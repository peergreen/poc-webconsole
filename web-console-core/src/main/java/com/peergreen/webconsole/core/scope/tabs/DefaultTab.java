package com.peergreen.webconsole.core.scope.tabs;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TabSheet;

import java.util.HashMap;
import java.util.Map;

/**
 * Default tab for scope tab view
 * @author Mohammed Boukada
 */
public class DefaultTab extends CssLayout {

    private TabSheet tabs;

    private Map<String, Component> modulesComponents = new HashMap<>();

    private Map<String, Component> modulesFrames = new HashMap<>();

    public DefaultTab(TabSheet tabs) {
        this.tabs = tabs;
        addStyleName("catalog");
        setSizeFull();
    }

    public void addModuleButtonInDefaultTab(final String scopeElementName, Component component) {
        Image moduleIcon = new Image(
                scopeElementName,
                new ThemeResource("img/default-module-icon.png"));
        CssLayout frame = new CssLayout();
        frame.addComponent(moduleIcon);
        frame.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component moduleComponent = modulesComponents.get(scopeElementName);
                if (tabs.getTab(moduleComponent) == null) {
                    tabs.addComponent(moduleComponent);
                    tabs.getTab(moduleComponent).setCaption(scopeElementName);
                    tabs.getTab(moduleComponent).setClosable(true);
                    tabs.setSelectedTab(moduleComponent);
                } else {
                    tabs.setSelectedTab(moduleComponent);
                }
            }
        });
        addComponent(frame);
        modulesComponents.put(scopeElementName, component);
        modulesFrames.put(scopeElementName, frame);
    }

    public void removeModuleButtonFromDefautlTab(String scopeElementName) {
        removeComponent(modulesFrames.get(scopeElementName));
    }
}
