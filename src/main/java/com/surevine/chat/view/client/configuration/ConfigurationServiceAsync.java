/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client.configuration;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>ConfigurationService</code>.
 * 
 * @see ConfigurationService
 */
public interface ConfigurationServiceAsync {
    /**
     * Starts an asynchronous call to the CredentialService.
     * 
     * @param callback
     *            the callback once the request has completed
     */
    void getConfiguration(AsyncCallback<Map<String, String>> callback);
}
