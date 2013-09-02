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
