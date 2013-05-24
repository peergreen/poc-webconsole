package com.peergreen.webconsole.core.notifier;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Window;

public class NotificationOverlay extends Window {

    public NotificationOverlay() {
        setContent(new CssLayout());
        setPrimaryStyleName("help-overlay");
        setDraggable(false);
        setResizable(false);
    }

    public void addComponent(Component c) {
        ((CssLayout) getContent()).addComponent(c);
    }

}
