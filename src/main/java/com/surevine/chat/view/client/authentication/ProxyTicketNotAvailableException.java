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

/**
 * Thrown if there was a problem retrieving a proxy ticket from the CAS server.
 */
public class ProxyTicketNotAvailableException extends
        CredentialsNotFoundException {

    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 2L;

    /**
     * The url to redirect the user to
     */
    private String redirectUrl;

    /**
     * Provide a default constructor so that GWT can un-serialise the object.
     */
    protected ProxyTicketNotAvailableException() {
        super();
    }

    /**
     * Initialise the exception with a message and url to which to redirect the
     * user.
     * 
     * @param message
     *            the message
     * @param redirectUrl
     *            the url. The catching code should redirect the user back to
     *            this url.
     */
    public ProxyTicketNotAvailableException(final String message,
            final String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }

    /**
     * Returns the redirect url. The catching code should proceed to redirect
     * the user to this url.
     * 
     * @return the url string.
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }
}
