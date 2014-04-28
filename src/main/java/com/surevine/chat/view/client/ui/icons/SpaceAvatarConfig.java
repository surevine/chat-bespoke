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

package com.surevine.chat.view.client.ui.icons;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.hablar.icons.client.AvatarConfig;
import com.surevine.chat.view.client.ChatClientPresenter;

public class SpaceAvatarConfig implements AvatarConfig {

    private ChatClientPresenter presenter;
    
    public void setPresenter(final ChatClientPresenter presenter) {
        this.presenter = presenter;
    }
    
    @Override
    public String getUrl(final XmppURI xmppURI, final String size) {
        if (presenter.getConfiguration() != null) {
            return presenter.getConfiguration().get("com.surevine.chatclient.spaceProtocol") + "://"
                    +presenter.getConfiguration().get("com.surevine.chatclient.spaceHost") +
                "/share/proxy/alfresco/sv-theme/user-profile/avatar?user=" +xmppURI.getNode() +"&size=" + getSize(size);
        }
        
        return null;
    }
    
    /**
     * Maps the hablar avatar size to an alfresco avatar size
     * @param size
     * @return
     */
    protected String getSize(final String size) {
        if(size.equals("tiny")) {
            return "smallAvatar";
        } else if(size.equals("medium")) {
            return "medium";
        } else {
            return "avatar";
        }
    }
}
