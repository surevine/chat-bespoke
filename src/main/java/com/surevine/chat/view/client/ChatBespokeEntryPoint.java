/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.events.PacketEvent;
import com.calclab.emite.core.client.events.PacketHandler;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURIFactory;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.hablar.chat.client.ChatConfig;
import com.calclab.hablar.client.HablarConfig;
import com.calclab.hablar.console.client.HablarConsole;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.HablarDisplay;
import com.calclab.hablar.core.client.HablarWidget;
import com.calclab.hablar.core.client.container.overlay.OverlayContainer;
import com.calclab.hablar.core.client.page.PagePresenter.Visibility;
import com.calclab.hablar.core.client.ui.menu.SimpleAction;
import com.calclab.hablar.dock.client.DockConfig;
import com.calclab.hablar.icons.client.AvatarProviderRegistry;
import com.calclab.hablar.rooms.client.HablarRoomsConfig;
import com.calclab.hablar.roster.client.RosterMessages;
import com.calclab.hablar.search.client.SearchConfig;
import com.calclab.hablar.search.client.query.NicknameContainsSearchQueryFactory;
import com.calclab.hablar.signals.client.notifications.BrowserPopupHablarNotifier;
import com.calclab.hablar.vcard.client.VCardConfig;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.RootPanel;
import com.surevine.chat.view.client.chat.ui.open.OpenSecureChatPresenter;
import com.surevine.chat.view.client.chat.ui.open.OpenSecureChatWidget;
import com.surevine.chat.view.client.roster.RosterMenuActions;
import com.surevine.chat.view.client.roster.RosterPresenceRemover;
import com.surevine.chat.view.client.search.SearchMenuActions;
import com.surevine.chat.view.client.search.WildcardAllTermsSearchQueryFactory;
import com.surevine.chat.view.client.ui.icons.SpaceAvatarConfig;

/**
 * The GWT module entry point for the Chat Client.
 */
public class ChatBespokeEntryPoint implements EntryPoint {

    private static final String DEFAULT_PRESENCE_PRIORITY = "1";

    private static final String NOTIFICATION_POPUP_HTML = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">"
            + "<html><head>"
            + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">"
            + "<style type=\"text/css\">"
            + "html { font-family: Tahoma, sans-serif; font-size: 80%; } "
            + "body { background-color: #FBC110; margin: 0; padding: 0;	} "
            + "#notificationContainer { width: 100%; height: 100%; } "
            + "#notificationContainer p.message { height: 30px; line-height: 30px; margin: 10px 10px 0 10px !important; padding: 0 2px;"
            + " overflow: hidden; white-space: nowrap; text-align: center; background-color: black; -moz-border-radius: 5px;"
            + " -webkit-border-radius: 5px; border-radius: 5px; } "
            + "#notificationContainer p.message a, #notificationContainer p.message a:visited { color: white; text-decoration: none; }"
            + "</style>"
            + "</head><body class=\"hablarPopupWindow\"><div id=\"notificationContainer\"></div></body></html>";
    
    /**
     * {@inheritDoc}.
     */
    public void onModuleLoad() {
        // Sets the default icon set
//        DefaultIcons.load();

        // Sets the HTML for the notification popup
        BrowserPopupHablarNotifier.setPopupHtml(NOTIFICATION_POPUP_HTML);

        final ChatBespokeGinjector ginjector = GWT
                .create(ChatBespokeGinjector.class);

        // CHAT-185: Chat IE6 browser error - possibly related to connection problems from one desktop system
        // Ensure that AutoConfig (not AutoConfigBoot) has definitely been run before we try and log in
        ginjector.getAutoConfig();
        
        String hideUsersString = PageAssist.getMeta("chatclient.hideusers");
        
        if((hideUsersString != null) && !"".equals(hideUsersString)) {
            RosterPresenceRemover remover = ginjector.getRosterPresenceRemover();
            
            String[] hideUsers = hideUsersString.split(":");
            
            for(String user : hideUsers) {
                XmppURI jid = XmppURI.jid(user);
                
                if(jid != null) {
                    remover.addUserToRemove(XmppURI.jid(user));
                }
            }
        }
        
        final XmppSession session = ginjector.getXmppSession();
        
        final ChatClientView chatClientView = new ChatClientWidget();
        final ChatClientPresenter presenter = new ChatClientPresenter(chatClientView, session);

        final HablarWidget hablarWidget = chatClientView.getHablar();
        final Hablar hablar = hablarWidget.getHablar();
        // Configuration
        config = new HablarConfig();
        config.layout = HablarDisplay.Layout.tabs;
        config.dockRoster = "left";
        config.chatConfig = ChatConfig.getFromMeta();
        config.roomsConfig = HablarRoomsConfig.getFromMeta();
        config.rosterConfig.oneClickChat = true;
        config.vcardConfig = VCardConfig.getFromMeta();
        config.dockConfig = DockConfig.getFromMeta();
        config.searchConfig = SearchConfig.getFromMeta();
        config.searchConfig.queryFactory = new NicknameContainsSearchQueryFactory();

        final XmppRoster roster = ginjector.getXmppRoster();
        final ChatManager chatManager = ginjector.getChatManager();
        
        final SearchMenuActions searchMenu = new SearchMenuActions(hablar,
                roster, chatManager, ginjector.getSecurityLabelManager(),
                ginjector.getXmppSecurityLabelExtensionFactory());
        config.searchConfig.searchActions = searchMenu;
        config.searchConfig.queryFactory = new WildcardAllTermsSearchQueryFactory();

        config.rosterConfig.rosterItemClickAction = new SimpleAction<RosterItem>(
                RosterMessages.msg.clickToChatWith(), "rosterItemClickAction") {
            @Override
            public void execute(final RosterItem item) {

                // Setup the Security Label Chooser page
                final OpenSecureChatPresenter openChat = new OpenSecureChatPresenter(
                        hablar.getEventBus(), chatManager,
                        ginjector.getSecurityLabelManager(),
                        ginjector.getXmppSecurityLabelExtensionFactory(),
                        new OpenSecureChatWidget(), item.getJID());
                hablar.addPage(openChat, OverlayContainer.ROL);

                openChat.requestVisibility(Visibility.focused);
            }
        };

        AvatarProviderRegistry registry = ginjector.getAvatarProviderRegistry();
        registry.put("space", new SpaceAvatarConfig() {{
            setPresenter(presenter);
        }});
        config.rosterConfig.avatarConfig = registry.getFromMeta();

        config.rosterConfig.rosterMenuActions = new RosterMenuActions(hablar,
                roster, chatManager, ginjector.getSecurityLabelManager(),
                ginjector.getXmppSecurityLabelExtensionFactory());

        /*
         * Add a hook to add any missing <priority> to outgoing <presence> stanzas 
         */
        session.addBeforeSendStanzaHandler(new PacketHandler() {
            
            @Override
            public void onPacket(PacketEvent event) {
                IPacket p = event.getPacket();
                
                if(p.getName().equals("presence") && p.getFirstChild("priority").equals(NoPacket.INSTANCE)) {
                    p.addChild("priority").setText(DEFAULT_PRESENCE_PRIORITY);
                }
            }
        });
        
        hablarWidget.setWidth("100%");
        hablarWidget.setHeight("100%");
        // Apply the configuration
        ChatClientHablar.install(hablarWidget.getHablar(), config, ginjector);

        // Add the console functionality if console=true is given as a request
        // param
        if ((Location.getParameter("console") != null)
                && (Location.getParameter("console").equals("true"))) {
            new HablarConsole(hablarWidget.getHablar(),
                    ginjector.getXmppConnection(), ginjector.getXmppSession());
        }

        final RootPanel rootPanel = RootPanel.get("chatclient_container");
        rootPanel.add(chatClientView.asWidget());
        
        this.exposeRosterAction();
        GWT.log("Exposed the roster action");
        
        // Use a deferred scheduled command to ensure that the host page is notified that the application is ready.
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                notifyHostPage();
            }
            
        });
    }
    
    private native void notifyHostPage() /*-{
        // Only make the call to the onGwtReady method if it has been setup.
        if (typeof $wnd.onGwtReady != 'undefined') {
            $wnd.onGwtReady();
        }
    }-*/;
    
    public native void exposeRosterAction() /*-{
        var that = this;
        $wnd.rosterAction = $entry(function(from) {
          that.@com.surevine.chat.view.client.ChatBespokeEntryPoint::executeRosterAction(Ljava/lang/String;)(from)
        });
    }-*/;
    
    public void executeRosterAction(final String uri) {
        if(uri == null) {
            return;
        }
        
        GWT.log("in the instance method with arg - " + uri);
        
        XmppURIFactory xmppURIFactory =  new XmppURIFactory();
        
        XmppURI xmppURI = xmppURIFactory.parse(uri);
        
        RosterItem target = new RosterItem(xmppURI, null, null, null);
        
        config.rosterConfig.rosterItemClickAction.execute(target);
    }
    
    private HablarConfig config;
    
}
