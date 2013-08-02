/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client;

import com.calclab.hablar.core.client.HablarDisplay;
import com.calclab.hablar.core.client.HablarWidget;
import com.calclab.hablar.core.client.pages.tabs.TabsLayout.TabHeaderSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The View of the MVP (Model-View-Presenter) pattern.
 */
public class ChatClientWidget extends Composite implements ChatClientView {

    /**
     * The ChatClientWidgetUiBinder interface.
     */
    interface ChatClientWidgetUiBinder extends
            UiBinder<Widget, ChatClientWidget> {
    }

    /**
     * The uiBinder.
     */
    private static ChatClientWidgetUiBinder uiBinder = GWT
            .create(ChatClientWidgetUiBinder.class);

    /**
     * The hablar <code>HablarWidget</code> UiField.
     */
    @UiField
    HablarWidget hablar;

    /**
     * The state <code>Label</code> UiField.
     */
    @UiField
    Label state;
    
    @UiField
    DivElement footer;

    /**
     * HablarWidget UiFactory.
     * 
     * @return A new <code>HablarWidget</code>
     */
    @UiFactory
    HablarWidget makeHablarWidget() {
        return new HablarWidget(HablarDisplay.Layout.tabs,
                TabHeaderSize.DEFAULT_SIZE);
    }

    /**
     * Default constructor.
     */
    public ChatClientWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    /**
     * {@inheritDoc}.
     */
    @Override
    public Widget asWidget() {
        return this;
    }

    /**
     * {@inheritDoc}.
     */
    public HablarWidget getHablar() {
        return hablar;
    }

    /**
     * {@inheritDoc}.
     */
    public HasText getState() {
        return state;
    }

    @Override
    public void setStateVisible(boolean isVisible) {
        footer.getStyle().setDisplay(isVisible ? Display.BLOCK : Display.NONE);
    }

}
