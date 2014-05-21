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

package com.surevine.chat.view.client;

import java.util.Map;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.SessionStates;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.surevine.chat.view.client.authentication.CredentialService;
import com.surevine.chat.view.client.authentication.CredentialServiceAsync;
import com.surevine.chat.view.client.authentication.ProxyTicketNotAvailableException;
import com.surevine.chat.view.client.authentication.UserCredentials;
import com.surevine.chat.view.client.configuration.ConfigurationService;
import com.surevine.chat.view.client.configuration.ConfigurationServiceAsync;

/**
 * The Presenter of the MVP (Model-View-Presenter) pattern.
 * 
 */
public class ChatClientPresenter {
    /**
     * How many milliseconds to delay retrying reconnects if the session gets disconnected
     */
    private static final int RETRY_DELAY_MILLIS = 5000;
    
    /**
     * Create a remote service proxy to talk to the server-side Configuration
     * service.
     */
    private final ConfigurationServiceAsync configurationService = GWT
            .create(ConfigurationService.class);

    /**
     * Create a remote service proxy to talk to the server-side Credential
     * service.
     */
    private final CredentialServiceAsync credentialService = GWT
            .create(CredentialService.class);

    /**
     * The xmpp session to use for communication.
     */
    private final XmppSession session;

    /**
     * The view object attached to this presenter.
     */
    private final ChatClientView view;

    /**
     * The number of unsuccessful login attempts.
     */
    private int loginAttempts = 0;

    /**
     * Whether to automatically attempt a login.
     */
    private boolean autoLogin = true;
    
    /**
     * Whether the chat client is currently connected
     */
    private boolean connected = false;
    
    /**
     * Servlet configuration.
     */
    private Map<String, String> configuration;
    
    /**
     * Constructor, setting up the handlers.
     * 
     * @param view
     *            The <code>ChateClientView</code>
     */
    @Inject
    public ChatClientPresenter(final ChatClientView view,
            final XmppSession session) {
        this.view = view;
        this.session = session;

        session.addSessionStateChangedHandler(true, new StateChangedHandler() {

            @Override
            public void onStateChanged(final StateChangedEvent event) {
                showState(session.getSessionState());
            }
        });

        Window.addWindowClosingHandler(new ClosingHandler() {
            @Override
            public void onWindowClosing(final ClosingEvent event) {
                autoLogin = false;
                session.logout();
            }
        });
    }

    /**
     * Login method.
     */
    private void login() {
        ++loginAttempts;

        GWT.log("Login attempt: " + loginAttempts);

        final ChatClientPresenter thisObject = this;

        // Load the configuration
        configurationService
                .getConfiguration(new AsyncCallback<Map<String, String>>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        // If we fail to load the configuration then our session
                        // with CAS has probably expired so we will reload the
                        // page so the user gets redirected back to cas

                        if ((Location.getParameter("reloaded") == null)
                                || !Location.getParameter("reloaded").equals(
                                        "true")) {
                            // We will add a parameter when we refresh the page
                            // so we don't accidentally get into a reload cycle
                            String href = Location.getHref();

                            if (Location.getParameterMap().isEmpty()) {
                                href += "?reloaded=true";
                            } else {
                                href += "&reloaded=true";
                            }

                            Location.assign(href);
                        } else {
                            Window
                                    .alert("An error occurred loading the configuration"
                                            + caught);
                        }
                    }

                    @Override
                    public void onSuccess(
                            final Map<String, String> configuration) {
                        thisObject.startChatClient(configuration);
                    }
                });
    }

    /**
     * Show the state of the session.
     * 
     * @param state
     *            The <code>State</code>
     */
    private void showState(final String state) {
        if (SessionStates.isDisconnected(state)) {
            if (autoLogin) {
                // If we were previously connected we will wait a couple of seconds before trying again
                if(connected) {
                    new Timer() {

                        @Override
                        public void run() {
                            login();
                        }
                        
                    }.schedule(RETRY_DELAY_MILLIS);
                } else {
                    login();
                }
            }
            
            connected = false;
        }
        
        if (SessionStates.isReady(state)) {
            loginAttempts = 0;
            connected = true;
        }
        
        view.setStateVisible(!SessionStates.isReady(state));

        view.getState().setText(state);
    }

    /**
     * Start the ChatClient.
     * 
     * @param configuration
     *            The configuration <code>Map</code>.
     */
    protected void startChatClient(final Map<String, String> configuration) {
        this.configuration = configuration;
        
        credentialService.getCredentials(new AsyncCallback<UserCredentials>() {
            public void onFailure(final Throwable caught) {
                if (caught instanceof ProxyTicketNotAvailableException) {
                    // If we get this far, but a proxy ticket isn't available
                    // then our session with cas has probably expired but our
                    // local session has not so we need to manually redirect to
                    // cas
                    final ProxyTicketNotAvailableException ePTNA = (ProxyTicketNotAvailableException) caught;

                    Location.assign(ePTNA.getRedirectUrl());
                } else {
                    ++loginAttempts;
                    GWT
                            .log("An error occurred loading the credentials",
                                    caught);
                }
            }

            public void onSuccess(final UserCredentials result) {
                final XmppURI xmppUri = XmppURI
                        .uri_or_null(result.getUsername(), configuration
                                .get("com.surevine.chatclient.jabberHost"),
                                null);

                GWT.log("Logged in okay");

                session.login(xmppUri, result.getPassword());
            }
        });
    }

    /**
     * Returns the view associated with this presenter
     * 
     * @return the view object
     */
    public ChatClientView getView() {
        return view;
    }
    
    /**
     * @return The {@link ConfigurationService}.
     */
    public Map<String, String> getConfiguration() {
        return configuration;
    }
}
