/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client.authentication;

import java.io.Serializable;

/**
 * Represents user credentials. This is used to transfer the user's credentials
 * in the CredentialService
 * 
 * @see CredentialService
 * 
 */
public class UserCredentials implements Serializable {
    /**
     * Serialisation UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The username.
     */
    private String username;

    /**
     * The password.
     */
    private String password;

    /**
     * Provide a default constructor to allow GWT to un-serialise the object.
     */
    @SuppressWarnings("unused")
    private UserCredentials() {
    }

    /**
     * Creates a new credentials object with the given username and password.
     * 
     * @param username
     *            the user's username
     * @param password
     *            the user's password
     */
    public UserCredentials(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username property from the object.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the user's password property from the object.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserCredentials other = (UserCredentials) obj;
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public String toString() {
        return "UserCredentials [username=" + username + "]";
    }
}
