package com.peergreen.webconsole.core.notifier;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Window;

/**
 * Notification Overlay
 * @author Mohammed Boukada
 */
public class NotificationOverlay extends Window {

    /**
     * Notification overlay constructor
     */
    public NotificationOverlay() {
        setContent(new CssLayout());
        setPrimaryStyleName("help-overlay");
        setDraggable(false);
        setResizable(false);
    }

    /**
     * Add component
     * @param c
     */
    public void addComponent(Component c) {
        ((CssLayout) getContent()).addComponent(c);
    }

}
