package com.peergreen.webconsole.modules.module2;

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
public class Module2 implements IViewContribution {
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
        return "osgi";
    }

    @Override
    public String getName() {
        return "Module 2";
    }
}
