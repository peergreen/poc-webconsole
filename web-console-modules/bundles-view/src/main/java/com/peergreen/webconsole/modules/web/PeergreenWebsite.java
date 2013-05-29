package com.peergreen.webconsole.modules.web;

import com.peergreen.webconsole.module.IModuleFactory;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 16/05/13
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
@org.apache.felix.ipojo.annotations.Component
@Provides
@Instantiate
public class PeergreenWebsite implements IModuleFactory {

    private List<String> allowedRoles = new ArrayList<String>() {{
        add("all");
    }};

    @Override
    public Component getView() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        BrowserFrame browser = new BrowserFrame("",
                new ExternalResource("http://www.peergreen.com/"));
        browser.setSizeFull();
        verticalLayout.addComponent(browser);
        verticalLayout.setExpandRatio(browser, 1.5f);
        return verticalLayout;
    }

    @Override
    public String getScope() {
        return "web";
    }

    @Override
    public String getName() {
        return "Peergreen Website";
    }

    @Override
    public List<String> getAllowedRoles() {
        return allowedRoles;
    }
}
