/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client.authentication;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface to allow the transfer of the user's username and password from the
 * server to the client.
 */
@RemoteServiceRelativePath("credentialService")
public interface CredentialService extends RemoteService {
    /**
     * Returns the user's credentials (a username and a password) which will be
     * used to authenticate the user against the xmpp server.
     * 
     * @return the user's credentials
     * @throws CredentialsNotFoundException
     *             the credentials could not be determined
     */
    UserCredentials getCredentials() throws CredentialsNotFoundException;
}
