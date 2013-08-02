/**
 * 
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
