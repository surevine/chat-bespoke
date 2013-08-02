/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client.authentication;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

/**
 * Test case for CredentialsNotFoundException.
 *
 * @see CredentialsNotFoundException
 */
public class CredentialsNotFoundExceptionTest {

    /**
     * Test instantiating the exception with a message.
     */
    @Test
    public void testCredentialsNotFoundExceptionString() {
        String message = "This is a test message";
        CredentialsNotFoundException e = new CredentialsNotFoundException(message);
        assertEquals("Message wasn't initialised or returned correctly", message, e.getMessage());
    }

    /**
     * Test instantiating the exception with a cause.
     */
    @Test
    public void testCredentialsNotFoundExceptionThrowable() {
        Throwable t = mock(Throwable.class);
        CredentialsNotFoundException e = new CredentialsNotFoundException(t);
        assertEquals("Throwable wasn't initialised or returned correctly", t, e.getCause());
    }

}
