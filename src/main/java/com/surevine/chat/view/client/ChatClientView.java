/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client;

import com.calclab.hablar.core.client.HablarWidget;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * The interface for the View of the MVP (Model-View-Presenter) pattern.
 */
public interface ChatClientView {

    /**
     * Get the widget.
     * 
     * @return The <code>Widget</code>
     */
    Widget asWidget();

    /**
     * Get the hablar.
     * 
     * @return The <code>HablarWidget</code>
     */
    HablarWidget getHablar();

    /**
     * Get the state.
     * 
     * @return The <code>HasText</code>
     */
    HasText getState();

    /**
     * Sets whether the session state should be displayed
     * @param isVisible
     */
    void setStateVisible(boolean isVisible);
}
