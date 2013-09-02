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
