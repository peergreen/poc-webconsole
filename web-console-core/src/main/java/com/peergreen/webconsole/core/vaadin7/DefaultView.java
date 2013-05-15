package com.peergreen.webconsole.core.vaadin7;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 14/05/13
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class DefaultView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setSizeFull();
        addStyleName("dashboard-view");

        Label header = new Label("Welcome to Peergreen Administration Console");
        header.addStyleName("h1");
        addComponent(header);
    }
}
