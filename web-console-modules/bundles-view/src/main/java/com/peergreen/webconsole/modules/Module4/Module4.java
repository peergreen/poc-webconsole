package com.peergreen.webconsole.modules.Module4;

import com.peergreen.webconsole.core.api.IModuleFactory;
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
public class Module4 implements IModuleFactory {
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
        return "Shell";
    }

    @Override
    public String getConsole() {
        return "pgadmin";
    }

    @Override
    public String getName() {
        return "Shell Manager";
    }
}
