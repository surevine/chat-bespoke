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
