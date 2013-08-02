/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
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
