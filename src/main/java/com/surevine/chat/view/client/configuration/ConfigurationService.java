/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client.configuration;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface to allow the transfer of the user's username and password from the
 * server to the client.
 */
@RemoteServiceRelativePath("configurationService")
public interface ConfigurationService extends RemoteService {
    /**
     * Returns the configuration for the chat client.
     * 
     * @return the configuration
     */
    Map<String, String> getConfiguration();
}
