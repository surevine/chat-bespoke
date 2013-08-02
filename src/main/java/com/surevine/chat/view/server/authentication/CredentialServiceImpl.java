/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.server.authentication;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.surevine.chat.view.client.authentication.CredentialService;
import com.surevine.chat.view.client.authentication.CredentialsNotFoundException;
import com.surevine.chat.view.client.authentication.ProxyTicketNotAvailableException;
import com.surevine.chat.view.client.authentication.UserCredentials;

/**
 * The server side implementation of the RPC service to retrieve the user
 * details which should have been placed in the session by the CAS filter.
 * 
 * The CAS ServiceURL can be set using the
 * <code>com.surevine.chatclient.server.authentication.serviceUrl</code> servlet
 * init parameter.
 */
public class CredentialServiceImpl extends RemoteServiceServlet implements
        CredentialService {
    /**
     * Serialisation UID.
     */
    private static final long serialVersionUID = 2L;

    /**
     * Class logger.
     */
    private static final Log LOG = LogFactory
            .getLog(CredentialServiceImpl.class);

    /**
     * The parameter name for the cas service parameter
     */
    static final String SERVICE_PARAMETER_NAME = "service";

    /**
     * The name of the servlet init param to change the service URL given to the
     * CAS proxy service. This should be the same as the service URL given to
     * the CAS filter
     */
    static final String SERVICE_URL_INIT_PARAM = "com.surevine.chatclient.server.authentication.serviceUrl";

    /**
     * The name of the servlet init param to change the cas server login url.
     * This will be used to construct the redirect to cas if required.
     */
    static final String CAS_SERVER_LOGON_URL_INIT_PARAM = "com.surevine.chatclient.server.authentication.casServerLoginUrl";

    /**
     * The service URL to use when requesting a proxy ticket.
     */
    private String serviceUrl;

    /**
     * The case server prefix to be used to construct redirects to the cas
     * server if required.
     */
    private String casServerLoginUrl;

    /**
     * {@inheritDoc}.
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Initialising " + SERVICE_URL_INIT_PARAM + " with "
                    + config.getInitParameter(SERVICE_URL_INIT_PARAM));
        }

        setServiceUrl(config.getInitParameter(SERVICE_URL_INIT_PARAM));

        if (LOG.isDebugEnabled()) {
            LOG.debug("Initialising " + CAS_SERVER_LOGON_URL_INIT_PARAM
                    + " with "
                    + config.getInitParameter(CAS_SERVER_LOGON_URL_INIT_PARAM));
        }

        casServerLoginUrl = config
                .getInitParameter(CAS_SERVER_LOGON_URL_INIT_PARAM);
    }

    /**
     * {@inheritDoc}.
     */
    public UserCredentials getCredentials() throws CredentialsNotFoundException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getCredentials Called");
        }

        // Get the local request object and the session
        final HttpServletRequest request = getRequest();
        final HttpSession session = request.getSession(true);

        // Get the assertion out of the session
        final Assertion assertion = (Assertion) session
                .getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);

        if (assertion == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("No Assertion found in the session");
            }

            throw new CredentialsNotFoundException(
                    "No AttributePrincipal found in the session");
        }

        // Get the authentication details from the session
        final AttributePrincipal principal = assertion.getPrincipal();

        if (principal == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("No AttributePrincipal found in the Assertion");
            }

            throw new CredentialsNotFoundException(
                    "No AttributePrincipal found in the session");
        }

        // Get the username out of the "principal"
        final String casUser = principal.getName();

        if (casUser == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("No username found in the AttributePrincipal");
            }

            throw new CredentialsNotFoundException(
                    "No username found in the AttributePrincipal");
        }

        // Retrieve a proxy ticket for the user
        final String proxyTicket = principal.getProxyTicketFor(serviceUrl);

        if (proxyTicket == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Could not retrieve a proxy ticket for serviceUrl "
                        + serviceUrl);
            }

            final String redirectUrl = CommonUtils.constructRedirectUrl(
                    casServerLoginUrl, SERVICE_PARAMETER_NAME, serviceUrl,
                    false, false);

            throw new ProxyTicketNotAvailableException(
                    "Could not retrieve a proxy ticket", redirectUrl);
        }

        return new UserCredentials(casUser, proxyTicket);
    }

    /**
     * Returns the request object for this servlet.
     * 
     * @return This servlet's request.
     */
    protected HttpServletRequest getRequest() {
        return getThreadLocalRequest();
    }

    /**
     * Gets the service URL for CAS.
     * 
     * @return The service URL.
     */
    protected String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * Sets the service URL for CAS.
     * 
     * @param serviceUrl
     *            The service URL.
     */
    protected void setServiceUrl(final String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
