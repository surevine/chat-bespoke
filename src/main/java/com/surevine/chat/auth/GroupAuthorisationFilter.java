/*
 * Copyright (C) 2010 Surevine Limited
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.surevine.chat.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import javax.servlet.Filter;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GroupAuthorisationFilter implements Filter {

    private static final String SESSION_KEY = "GroupAuthorisationOK";

    // We're opening a new request every time. The reasoning being we'd like to
    // work
    // without managing the potential termination of longer lived connections,
    // and
    // we don't mind the additional performance hit because we'll only connect
    // to LDAP
    // once per session. We also accept that this means that someone can keep a
    // chat session
    // going indefinatley if they are removed rom the chat-users LDAP group
    // mid-session
    private String _rootDN;
    private String _rootPW;
    private String _host;
    private String _rootContext;
    private String _groupName;
    
    private static final Log _logger = LogFactory.getLog(GroupAuthorisationFilter.class);

    public void init(FilterConfig filterConfig) {
        _logger.debug("Initialising filter");
        _rootDN = filterConfig.getInitParameter("connectDistinguisedName");
        _rootPW = filterConfig.getInitParameter("connectPassword");
        _host = filterConfig.getInitParameter("LdapHostname");
        _rootContext = filterConfig.getInitParameter("rootContext");
        _groupName = filterConfig.getInitParameter("groupName");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) {
        try {
            HttpSession session = ((HttpServletRequest) request).getSession();
            if (session.getAttribute(SESSION_KEY) != null) {
                _logger.debug("Session attribute set:  Allowing");
                chain.doFilter(request, response);
            }
            // If we don't have a user in the session, just continue. This
            // special
            // case is what allows us to work with CAS,
            // _requires_ something like CAS to be secure, and is the reason
            // that we
            // need to code our own filter for this.
            else if (((HttpServletRequest) request).getRemoteUser() == null) {
                _logger.debug("User principal not set - allowing");
                chain.doFilter(request, response);
            } else {

                String userName = ((HttpServletRequest) request).getRemoteUser();
                _logger.debug("Looking up group membership for "+userName);
                if (getGroupMembers(_groupName).contains(userName)) {
                    _logger.info("Allowing "+userName+" based on group membership");
                    chain.doFilter(request, response);
                } else {
                    _logger.info("Forbidden to "+userName);
                    ((HttpServletResponse) response).sendError(403,
                            "You are not permitted to use this application");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error occured during group authorisation: " + e, e);
        }
    }

    protected InitialDirContext getLdapConnection() throws NamingException {
        Properties ldapEnv = new Properties();
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL, "ldap://" + _host + "/"
                + _rootContext);
        ldapEnv.put(Context.SECURITY_PRINCIPAL, _rootDN);
        ldapEnv.put(Context.SECURITY_CREDENTIALS, _rootPW);
        return new InitialDirContext(ldapEnv);

    }

    /**
     * Get a list of the members of a group, searching for the group using an
     * LDAP filter expression and scope.
     * 
     * @param filter
     *            LDAP search filter (see RFC2254)
     * @param scope
     *            One of SearchControls.OBJECT_SCOPE,
     *            SearchControls.ONELEVEL_SCOPE, or SearchControls.SUBTREE_SCOPE
     *            (see javax.naming.directory.SearchControls)
     * @return List of usernames
     * @throws NamingException
     * @throws LdapException
     *             On any LDAP error
     */
    private Collection<String> getGroupMembers(final String groupName)
            throws NamingException {
        _logger.debug("Looking for members of "+groupName);
        String filter = "cn=" + groupName;
        Collection<String> memberList = new HashSet<String>(20);

        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> objects;
        DirContext ctx = getLdapConnection();

        objects = ctx.search("ou=groups", filter, controls);

        while (objects.hasMore()) {
            SearchResult sr = (SearchResult) objects.next();
            Attributes attributes = sr.getAttributes();
            Attribute attribute = attributes.get("member");

            if (attribute != null) {
                NamingEnumeration<?> valueEnum = attribute.getAll();

                while (valueEnum.hasMore()) {
                    String value = valueEnum.next().toString();

                    final String searchFor = "cn=";
                    int start = value.indexOf(searchFor);
                    int end = value.indexOf(',', start);

                    if (start >= 0 && end >= 0) {
                        String name = value.substring(
                                start + searchFor.length(), end);
                        _logger.debug(name+" is a chatter");
                        memberList.add(name);
                    }
                }
            }
        }
        _logger.debug("Returning a total of "+memberList.size()+" chatters");
        return memberList;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }
}
