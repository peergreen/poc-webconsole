package com.peergreen.webconsole.core.vaadin7;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 20/05/13
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class BadRequestView extends CssLayout implements View {

    private String badRequest;

    public BadRequestView() {
        setSizeFull();
        Label message = new Label("Sorry, there is no related content for the request \"" + badRequest +"\" !");
        message.setStyleName("h1");
        addComponent(message);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public void setBadRequest(String badRequest) {
        this.badRequest = badRequest;
    }
}
