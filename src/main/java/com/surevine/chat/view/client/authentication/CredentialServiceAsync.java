/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client.authentication;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>CredentialService</code>.
 * 
 * @see CredentialService
 */
public interface CredentialServiceAsync {
    /**
     * Starts an asynchronous call to the CredentialService.
     * 
     * @param callback
     *            the callback once the request has completed
     */
    void getCredentials(AsyncCallback<UserCredentials> callback);
}
