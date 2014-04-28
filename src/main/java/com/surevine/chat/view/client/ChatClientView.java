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
