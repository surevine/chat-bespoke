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

import com.calclab.hablar.icons.client.AvatarProviderRegistry;
import com.google.gwt.inject.client.GinModules;
import com.surevine.chat.common.xmpp.ChatCommonGinModule;
import com.surevine.chat.view.client.roster.RosterPresenceRemover;

@GinModules({ChatBespokeGinModule.class,ChatCommonGinModule.class})
public interface ChatBespokeGinjector extends ChatClientGinjector {
    
    AvatarProviderRegistry getAvatarProviderRegistry();
    
    RosterPresenceRemover getRosterPresenceRemover();
}
