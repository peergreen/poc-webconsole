package com.peergreen.webconsole.modules.module2;

import static java.lang.String.format;

import com.peergreen.webconsole.module.IModuleFactory;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.osgi.framework.BundleContext;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

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
public class Module2 implements IModuleFactory {

    private final BundleContext bundleContext;
    private List<String> allowedRoles = new ArrayList<String>() {{
        add("all");
    }};

    public Module2(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public Component getView() {
        CssLayout component = new CssLayout();
        component.addStyleName("timeline");

        Label header = new Label(format("This view contribution comes from Bundle %s ! ",
                                        bundleContext.getBundle().getSymbolicName()));
        header.addStyleName("h1");
        component.addComponent(header);
        return component;
    }

    @Override
    public String getScope() {
        return "security";
    }

    @Override
    public String getName() {
        return "Security Manager";
    }

    @Override
    public List<String> getAllowedRoles() {
        return allowedRoles;
    }
}
