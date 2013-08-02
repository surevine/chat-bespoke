package com.surevine.chat.view.client.signals.notifications;

import java.util.ArrayList;

import com.calclab.hablar.core.client.browser.BrowserFocusHandler;
import com.calclab.hablar.signals.client.SignalMessages;
import com.calclab.hablar.signals.client.notifications.BrowserPopupHablarNotifier;
import com.calclab.hablar.signals.client.notifications.HablarNotifier;

/**
 * A {@link HablarNotifier} which uses browser popups to notify the user of new
 * messages. Includes code to bypass the document.domain issue with popup
 * windows in IE.
 * <p>
 * This is done by opening the window to the url of a document containing a
 * script to set the document domain of the popup window before calling back
 * into this code to the trigger the write.
 */
public class IEBrowserPopupHablarNotifier extends BrowserPopupHablarNotifier {

    /**
     * This is a list of messages to display once we have the ability to write
     * to the popup.
     */
    private ArrayList<String> queuedMessages;

    /**
     * A flag to signal whether the popup is currently accesible:
     * <dl>
     * <dt><code>null</code></dt>
     * <dd>We are not sure if the popup is writeable or not, or the popup is not
     * displayed.</dd>
     * 
     * <dt><code>false</code></dt>
     * <dd>The popup is displayed but not yet writeable</dd>
     * 
     * <dt><code>true</code></dt>
     * <dd>The popup is writeable</dd>
     */
    private Boolean popupWriteable;

    /**
     * Returned by {@link #createToasterWindow(int, int, String)} if the
     * document.domain is set such that we do not have immediate write access to
     * the window.
     */
    private static final int CREATE_TOASTER_WINDOW_WAIT_FOR_CALLBACK = 1024;

    public IEBrowserPopupHablarNotifier() {
        super();

        queuedMessages = new ArrayList<String>();
        popupWriteable = null;

        registerCallback();
    }

    @Override
    public void show(final String userMessage, final String messageType) {
        // Show the message only if the browser window doesn't have focus.
        if (!BrowserFocusHandler.getInstance().hasFocus()) {
            // If popupWriteable is false then we have opened the popup but
            // can't yet access it so just queue the message
            if (popupWriteable != null && !popupWriteable) {
                queuedMessages.add(userMessage);
                return;
            }

            final int success = createToasterWindow(POPUP_WIDTH,
                    POPUP_MESSAGE_HEIGHT + POPUP_STATIC_HEIGHT,
                    SignalMessages.msg.browserPopupNotifierWindowTitle());

            if (success != CREATE_TOASTER_WINDOW_FAILURE) {
                if (success == CREATE_TOASTER_WINDOW_WAIT_FOR_CALLBACK) {
                    queuedMessages.add(userMessage);
                    popupWriteable = false;
                    return;
                } else if (success == CREATE_TOASTER_WINDOW_NEWLY_OPENED) {
                    // If we have just opened a new window, reset the message
                    // count
                    messageCount = 0;
                }
                addMessage(userMessage);
            }
        }
    }

    /**
     * Registers the callback handler with the browser window object.
     */
    // @formatter:off
    private native void registerCallback() /*-{
		var that = this;

		$wnd.browserPopupHablarNotifierCallback = $entry(function() {
			that.@com.surevine.chat.view.client.signals.notifications.IEBrowserPopupHablarNotifier::popupWindowLoadedCallback()();
		});
    }-*/;

    // @formatter:on

    /**
     * The method that is called once the popup window has set its <code>document.domain</code>.
     */
    private void popupWindowLoadedCallback() {
        initialiseWindow(SignalMessages.msg.browserPopupNotifierWindowTitle());

        popupWriteable = true;

        for (String message : queuedMessages) {
            addMessage(message);
        }

        queuedMessages.clear();
    }

    /**
     * Creates the popup window if it's not already open
     * 
     * @param width
     * @param height
     * @param htmlPage
     * @param title
     * @return
     */
    // @formatter:off
    private native int createToasterWindow(final int width, final int height,
            final String title) /*-{
		try {
			// If the toaster window is already open, our work is done!
			if ($wnd.toasterWindow && !$wnd.toasterWindow.closed) {
				return @com.calclab.hablar.signals.client.notifications.BrowserPopupHablarNotifier::CREATE_TOASTER_WINDOW_ALREDY_OPEN;
			}

			var screenWidth = $wnd.screen.width;
			var screenHeight = $wnd.screen.height;

			var left = screenWidth - width;
			var top = screenHeight - height;

			// We will include a random element to the window handle
			// just in case we have multiple chat clients running within
			// one browser instance
			var windowHandle = "chatNotifierPopup"
					+ Math.floor(Math.random() * 1000000);

			var url = null;
			var usingCallback = false;

			// If the window location domain is different from the document domain
			if ($doc.domain && $doc.domain != $wnd.location.hostname) {
				url = "popup.html?d=" + $doc.domain;
				usingCallback = true;
			}

			$wnd.toasterWindow = $wnd
					.open(
							url,
							windowHandle,
							"status=no,toolbar=no,location=yes,menubar=no,directories=no,resizable=yes,width="
									+ width
									+ ",height="
									+ height
									+ ",left="
									+ left
									+ ",top="
									+ top
									+ ",alwaysRaised=yes,scrollbars=yes");

			if ($wnd.toasterWindow == null) {
				// Popups must be blocked :(
				return @com.calclab.hablar.signals.client.notifications.BrowserPopupHablarNotifier::CREATE_TOASTER_WINDOW_FAILURE;
			}

			if (!usingCallback) {
				this.@com.surevine.chat.view.client.signals.notifications.IEBrowserPopupHablarNotifier::initialiseWindow(Ljava/lang/String;)(title);

				return @com.calclab.hablar.signals.client.notifications.BrowserPopupHablarNotifier::CREATE_TOASTER_WINDOW_NEWLY_OPENED;
			} else {
				return @com.surevine.chat.view.client.signals.notifications.IEBrowserPopupHablarNotifier::CREATE_TOASTER_WINDOW_WAIT_FOR_CALLBACK;
			}

		} catch (e) {
			return @com.calclab.hablar.signals.client.notifications.BrowserPopupHablarNotifier::CREATE_TOASTER_WINDOW_FAILURE;
		}
    }-*/;
    // @formatter:on

    /**
     * Initialises the popup window (sets the title and layout, and attempts to focus it)
     * @param title the window title
     */
    // @formatter:off
    private native void initialiseWindow(final String title) /*-{
		if ($wnd.toasterWindow.document) {
			$wnd.toasterWindow.document
					.write(@com.calclab.hablar.signals.client.notifications.BrowserPopupHablarNotifier::popupHtml);
			$wnd.toasterWindow.document.title = title;
		}
		$wnd.toasterWindow.focus();
    }-*/;
    // @formatter:on
}
