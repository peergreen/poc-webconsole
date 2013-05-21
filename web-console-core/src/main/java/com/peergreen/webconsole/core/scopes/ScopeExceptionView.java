package com.peergreen.webconsole.core.scopes;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 19/05/13
 * Time: 12:58
 * To change this template use File | Settings | File Templates.
 */
public class ScopeExceptionView extends CssLayout implements View {

    public ScopeExceptionView(Exception e) {
        setSizeFull();
        Label eMessage = new Label(e.getMessage());
        eMessage.setStyleName("h1");
        Label eStack = new Label(e.getStackTrace().toString());
        addComponent(eMessage);
        addComponent(eStack);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
