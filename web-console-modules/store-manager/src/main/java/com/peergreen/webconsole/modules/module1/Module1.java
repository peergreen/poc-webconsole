package com.peergreen.webconsole.modules.module1;

import com.peergreen.webconsole.module.IModuleFactory;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import java.util.ArrayList;
import java.util.List;

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
public class Module1 implements IModuleFactory {

    private List<String> allowedRoles = new ArrayList<String>() {{
        add("all");
    }};

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

    @Override
    public List<String> getAllowedRoles() {
        return allowedRoles;
    }
}
