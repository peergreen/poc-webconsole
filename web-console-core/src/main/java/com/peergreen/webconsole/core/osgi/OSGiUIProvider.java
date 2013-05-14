package com.peergreen.webconsole.core.osgi;

import com.peergreen.webconsole.core.vaadin7.MainUI;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public class OSGiUIProvider extends UIProvider {

    private static final long serialVersionUID = 1451931523729856181L;
    private static final OSGiUIProvider INSTANCE = new OSGiUIProvider();

    private OSGiUIProvider() {}

    public static OSGiUIProvider instance() {
        return INSTANCE;
    }

    @Override
    public Class<? extends UI> getUIClass(final UIClassSelectionEvent event) {
        return MainUI.class;
    }

    @Override
    public UI createInstance(final UICreateEvent e) {
        UI ui = new MainUI();
        return ui;
    }

}
