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

package com.surevine.chat.view.server.configuration;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.surevine.chat.view.client.configuration.ConfigurationService;

/**
 * The server side implementation of the RPC service to retrieve the user
 * details which should have been placed in the session by the CAS filter.
 * 
 * The CAS ServiceURL can be set using the
 * <code>com.surevine.chatclient.server.authentication.serviceUrl</code> servlet
 * init parameter.
 */
public class ConfigurationServiceImpl extends RemoteServiceServlet implements
        ConfigurationService {
    /**
     * Serialisation UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class logger.
     */
    private static final Log LOG = LogFactory
            .getLog(ConfigurationServiceImpl.class);

    /**
     * The name of the servlet init param to change the service URL given to the
     * CAS proxy service. This should be the same as the service URL given to
     * the CAS filter.
     */
    static final String SERVICE_URL_INIT_PARAM = "com.surevine.chatclient.server.authentication.serviceUrl";

    /**
     * {@inheritDoc}.
     */
    public Map<String, String> getConfiguration() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Reading configuration");
        }

        // This will hold the key/value configuration pairs
        final Map<String, String> configuration = new HashMap<String, String>();

        @SuppressWarnings("unchecked")
        final Enumeration<String> parameterNames = getInitParameterNames();

        String parameterName;

        while (parameterNames.hasMoreElements()) {
            parameterName = parameterNames.nextElement();

            configuration.put(parameterName, getInitParameter(parameterName));

            if (LOG.isDebugEnabled()) {
                LOG.debug("Found config parameter: " + parameterName + " = "
                        + getInitParameter(parameterName));
            }
        }

        return configuration;
    }

}
