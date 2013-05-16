package com.peergreen.webconsole.modules.module1;

import com.peergreen.webconsole.core.api.IViewContribution;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 14/05/13
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
@org.apache.felix.ipojo.annotations.Component
@Provides
@Instantiate
public class Module1 implements IViewContribution {
    @Override
    public com.vaadin.ui.Component getView() {
        CssLayout component = new CssLayout();
        component.addStyleName("timeline");

        Label header = new Label("This is a view contribution ! ");
        header.addStyleName("h1");
        component.addComponent(header);
        return component;
    }

    @Override
    public String getScope() {
        return "store";
    }

    @Override
    public String getName() {
        return "Store Manager";
    }
}
