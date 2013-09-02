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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test case for <code>UserCredentials</code>.
 *
 * @see UserCredentials
 */
public class UserCredentialsTest {

    /**
     * The test username.
     */
    private static final String TEST_USERNAME = "testUsername";
    /**
     * The test password.
     */
    private static final String TEST_PASSWORD = "testPassword";

    /**
     * Test that a UserCredentials object is correctly instantiated.
     */
    @Test
    public void testUserCredentials() {
        UserCredentials credentials = new UserCredentials(TEST_USERNAME, TEST_PASSWORD);

        assertEquals("Username wasn't initialised or returned correctly", TEST_USERNAME,
                credentials.getUsername());
        assertEquals("Password wasn't initialised or returned correctly", TEST_PASSWORD,
                credentials.getPassword());
    }

    /**
     * Test the toString method of the UserCredentials class.
     */
    @Test
    public void testToString() {
        UserCredentials credentials = new UserCredentials(TEST_USERNAME, TEST_PASSWORD);

        String result = credentials.toString();
        assertTrue("Username does not appear in toString", result.indexOf(TEST_USERNAME) != -1);
        assertFalse("Password appears in toString", result.indexOf(TEST_PASSWORD) != -1);
    }

    /**
     * Test the hashCode method of the UserCredentials class.
     */
    @Test
    public void testHashCode() {
        String username1 = "testUsername1";
        String password1 = "testPassword1";

        String username2 = "testUsername2";
        String password2 = "testPassword2";

        UserCredentials credentials1 = new UserCredentials(username1, password1);
        UserCredentials credentials2 = new UserCredentials(username1, password1);
        UserCredentials credentials3 = new UserCredentials(username2, password2);

        assertTrue("hashCode returns different codes for objects with same properties ["
                + credentials1.hashCode() + " != " + credentials2.hashCode() + "]", credentials1
                .hashCode() == credentials2.hashCode());
        assertTrue("hashCode returns same codes for objects with different properties ["
                + credentials1.hashCode() + " == " + credentials3.hashCode() + "]", credentials1
                .hashCode() != credentials3.hashCode());
    }

    /**
     * Test the equals method of the UserCredentials class.
     */
    @Test
    public void testEquals() {
        String username1 = "testUsername1";
        String password1 = "testPassword1";

        String username2 = "testUsername2";
        String password2 = "testPassword2";

        UserCredentials credentials1 = new UserCredentials(username1, password1);
        UserCredentials credentials2 = new UserCredentials(username1, password1);
        UserCredentials credentials3 = new UserCredentials(username2, password2);

        assertTrue("equals returns false for objects with same properties", credentials1
                .equals(credentials2));
        assertFalse("equals returns true for objects with different properties", credentials1
                .equals(credentials3));
    }

}
