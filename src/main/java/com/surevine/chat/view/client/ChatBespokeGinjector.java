package com.surevine.chat.view.client;

import com.calclab.hablar.icons.client.AvatarProviderRegistry;
import com.google.gwt.inject.client.GinModules;
import com.surevine.chat.common.xmpp.ChatCommonGinModule;
import com.surevine.chat.view.client.roster.RosterPresenceRemover;

@GinModules({ChatBespokeGinModule.class,ChatCommonGinModule.class})
public interface ChatBespokeGinjector extends ChatClientGinjector {
    
    AvatarProviderRegistry getAvatarProviderRegistry();
    
    RosterPresenceRemover getRosterPresenceRemover();
}
