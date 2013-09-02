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
