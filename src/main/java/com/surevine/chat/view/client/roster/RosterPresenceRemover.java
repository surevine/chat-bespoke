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

package com.surevine.chat.view.client.roster;

import java.util.HashSet;
import java.util.Set;

import com.calclab.emite.core.client.conn.StanzaEvent;
import com.calclab.emite.core.client.conn.StanzaHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

/**
 * Class which will listen for incoming stanzas and remove certain users from the roster.
 * @author ashleyw
 */
public class RosterPresenceRemover {
    private Set<XmppURI> jidsToRemove;
    
    private class RosterResultListener implements StanzaHandler {

        @Override
        public void onStanza(StanzaEvent event) {
            IPacket stanza = event.getStanza();
            
            if(!stanza.getName().equals("iq")) {
                return;
            }
            
            IQ iq = new IQ(stanza);
            
            if(iq.isType(Type.result)) {
                IPacket query = iq.getFirstChild(MatcherFactory.byNameAndXMLNS("query", "jabber:iq:roster"));
                
                if(query != NoPacket.INSTANCE) {
                    for(IPacket item : query.getChildren(MatcherFactory.byName("item"))) {
                        if(jidsToRemove.contains(XmppURI.jid(item.getAttribute("jid")))) {
                            query.removeChild(item);
                        }
                    }
                }
            }
        }
        
    }
    
    /**
     * Instatiates the class. This will register handlers with the session to remove any presence
     * or roster notifications about the given jids.
     * @param connection
     */
    @Inject
    public RosterPresenceRemover(final XmppConnection connection) {
        this.jidsToRemove = new HashSet<XmppURI>();
        GWT.log("Initialising Roster Presence Remover");
        
        connection.addStanzaReceivedHandler(new RosterResultListener());
    }

    /**
     * Adds a user to the set of jids which should be removed from any incoming roster results.
     * @param jid the jid of the user to remove
     */
    public void addUserToRemove(final XmppURI jid) {
        jidsToRemove.add(jid);
    }
}
