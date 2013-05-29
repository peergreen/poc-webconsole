package com.peergreen.webconsole.core.scope.tabs;

import com.peergreen.webconsole.module.IModuleFactory;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TabSheet;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 28/05/13
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
public class DefaultTab extends CssLayout {

    private TabSheet tabs;

    private HashMap<IModuleFactory, Component> modulesComponents = new HashMap<>();

    private HashMap<IModuleFactory, Component> modulesFrames = new HashMap<>();

    public DefaultTab(TabSheet tabs) {
        this.tabs = tabs;
        addStyleName("catalog");
    }

    public void addModuleButtonInDefaultTab(final IModuleFactory moduleFactory, Component component) {
        Image moduleIcon = new Image(
                moduleFactory.getName(),
                new ThemeResource("img/default-module-icon.png"));
        CssLayout frame = new CssLayout();
        frame.addComponent(moduleIcon);
        frame.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component moduleComponent = modulesComponents.get(moduleFactory);
                if (tabs.getTab(moduleComponent) == null) {
                    tabs.addComponent(moduleComponent);
                    tabs.getTab(moduleComponent).setCaption(moduleFactory.getName());
                    tabs.getTab(moduleComponent).setClosable(true);
                    tabs.setSelectedTab(moduleComponent);
                } else {
                    tabs.setSelectedTab(moduleComponent);
                }
            }
        });
        addComponent(frame);
        modulesComponents.put(moduleFactory, component);
        modulesFrames.put(moduleFactory, frame);
    }

    public void removeModuleButtonFromDefautlTab(IModuleFactory moduleFactory) {
        removeComponent(modulesFrames.get(moduleFactory));
    }
}
