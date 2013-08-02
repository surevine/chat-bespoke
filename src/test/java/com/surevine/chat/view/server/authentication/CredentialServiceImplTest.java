/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.server.authentication;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.junit.Before;
import org.junit.Test;

import com.surevine.chat.view.client.authentication.CredentialsNotFoundException;
import com.surevine.chat.view.client.authentication.ProxyTicketNotAvailableException;
import com.surevine.chat.view.client.authentication.UserCredentials;

/**
 * Test case for CredentialServiceImpl servlet.
 *
 * @see CredentialServiceImpl
 */
public class CredentialServiceImplTest {

    /**
     * The (partially mocked) class under test.
     */
    private CredentialServiceImpl credentialService;

    /**
     * The (mocked) request.
     */
    private HttpServletRequest request;

    /**
     * The (mocked) session.
     */
    private HttpSession session;

    /**
     * The (mocked) config.
     */
    private ServletConfig config;

    /**
     * The (mocked) assertion.
     */
    private Assertion assertion;

    /**
     * The (mocked) principal.
     */
    private AttributePrincipal principal;

    /**
     * The test proxy.
     */
    private static final String TEST_PROXY_RETURN = "proxyTicket";

    /**
     * The test service url.
     */
    private static final String TEST_SERVICE_URL = "http://testserviceurl/";

    /**
     * The test cas login url.
     */
    private static final String TEST_LOGIN_URL = "http://cas/login";

    /**
     * The test username.
     */
    private static final String TEST_USERNAME = "userName";

    /**
     * Initialise the class under test.
     *
     * @throws CredentialsNotFoundException
     *             When getCredentials fails.
     * @throws ServletException
     *             When credentialService initialisation fails.
     */
    @Before
    public void setUp() throws CredentialsNotFoundException, ServletException {
        // We will be using a partially mocked credential service
        credentialService = mock(CredentialServiceImpl.class);

        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        config = mock(ServletConfig.class);
        assertion = mock(Assertion.class);
        principal = mock(AttributePrincipal.class);

        // Initialise the mock request to return the session
        when(request.getSession(true)).thenReturn(session);

        // Initialise the config to return the appropriate init parameter
        when(config.getInitParameter(CredentialServiceImpl.SERVICE_URL_INIT_PARAM)).thenReturn(
                TEST_SERVICE_URL);
        when(config.getInitParameter(CredentialServiceImpl.CAS_SERVER_LOGON_URL_INIT_PARAM)).thenReturn(
        		TEST_LOGIN_URL);

        // Initialise the assertion to return the principal
        when(assertion.getPrincipal()).thenReturn(principal);

        // Initialise the cas receipt
        when(principal.getName()).thenReturn(TEST_USERNAME);
        when(principal.getProxyTicketFor(TEST_SERVICE_URL)).thenReturn(TEST_PROXY_RETURN);

        // Initialise the session to return the CASReceipt
        when(session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION)).thenReturn(assertion);

        // Initialise the credentialService
        when(credentialService.getRequest()).thenReturn(request);

        // And pass through all the unmocked methods
        doCallRealMethod().when(credentialService).setServiceUrl(anyString());
        when(credentialService.getServiceUrl()).thenCallRealMethod();
        doCallRealMethod().when(credentialService).init(config);
        when(credentialService.getCredentials()).thenCallRealMethod();

        // Initialise the servlet
        credentialService.init(this.config);
    }

    /**
     * Tests that the servlet has been correctly initialised.
     */
    @Test
    public void testServletIsInitialised() {
        // Check that the servlet has been properly initialised

        assertEquals(TEST_SERVICE_URL, credentialService.getServiceUrl());
    }

    /**
     * Tests the getCredentials method.
     *
     * @throws CredentialsNotFoundException
     *             When getCredentials fails.
     */
    @Test
    public void testGetCredentials() throws CredentialsNotFoundException {
        UserCredentials credentials = credentialService.getCredentials();

        verify(session).getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);

        assertEquals("The username", credentials.getUsername(), TEST_USERNAME);
        assertEquals(credentials.getPassword(), TEST_PROXY_RETURN);
    }

    /**
     * Tests the getCredentials method if no receipt in the session ticket could be found.
     *
     * @throws CredentialsNotFoundException
     *             When getCredentials fails.
     */
    @Test(expected = CredentialsNotFoundException.class)
    public void testGetCredentialsWithNoSession() throws CredentialsNotFoundException {
        when(session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION)).thenReturn(null);

        credentialService.getCredentials();
    }

    /**
     * Tests the getCredentials method if no username is available.
     *
     * @throws CredentialsNotFoundException
     *             When getCredentials fails.
     */
    @Test(expected = CredentialsNotFoundException.class)
    public void testGetCredentialsNullUsername() throws CredentialsNotFoundException {
        when(principal.getName()).thenReturn(null);

        credentialService.getCredentials();
    }

    /**
     * Tests the getCredentials method if no proxy ticket can be found.
     *
     * @throws CredentialsNotFoundException
     *             When getCredentials fails.
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testGetCredentialsNullProxyTicket() throws CredentialsNotFoundException, UnsupportedEncodingException {
    	boolean exceptionCaught = false;
    	
    	try {
	        when(principal.getProxyTicketFor(TEST_SERVICE_URL)).thenReturn(null);
	
	        credentialService.getCredentials();
    	}
    	catch(ProxyTicketNotAvailableException e) {
    		exceptionCaught = true;
    		
    		assertEquals("Incorrect redirect url", TEST_LOGIN_URL + "?service=" + URLEncoder.encode(TEST_SERVICE_URL, "UTF-8"), e.getRedirectUrl());
    	}
    	
    	assertEquals("Expected exception not thrown", true, exceptionCaught);
    }
}
