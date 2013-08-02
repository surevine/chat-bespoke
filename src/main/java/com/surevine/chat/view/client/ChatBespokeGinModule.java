package com.surevine.chat.view.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.surevine.chat.view.client.roster.RosterPresenceRemover;

public class ChatBespokeGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(RosterPresenceRemover.class).asEagerSingleton();
    }
}
