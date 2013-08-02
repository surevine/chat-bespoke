/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
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
