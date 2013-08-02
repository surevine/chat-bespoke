/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client.authentication;

/**
 * This exception is used to represent the situation where a user's credentials
 * could not be found for some reason. Either the data has not be passed into
 * the session by the CASFilter
 */
public class CredentialsNotFoundException extends Exception {
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Provide a default constructor so that GWT can un-serialise the object.
     */
    protected CredentialsNotFoundException() {
        super();
    }

    /**
     * Initialise the exception with a message and cause.
     * 
     * @param message
     *            the message
     */
    public CredentialsNotFoundException(final String message) {
        super(message);
    }

    /**
     * Initialise the exception with a cause.
     * 
     * @param cause
     *            the chained exception which caused this exception
     */
    public CredentialsNotFoundException(final Throwable cause) {
        super(cause);
    }
}
