package com.peergreen.webconsole.tabs;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

import java.util.HashMap;
import java.util.Map;

/**
 * Default tab for scope tab view
 * @author Mohammed Boukada
 */
public class DefaultTab extends CssLayout {

    private TabSheet tabs;
    private UI ui;

    private Map<String, Component> modulesComponents = new HashMap<>();
    private Map<String, Component> modulesFrames = new HashMap<>();

    public DefaultTab(TabSheet tabs) {
        this.tabs = tabs;
        addStyleName("catalog");
        setSizeFull();
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public void addExtension(Component component, final String tabName) {
        Image moduleIcon = new Image(
                tabName,
                new ThemeResource("img/default-module-icon.png"));
        final CssLayout frame = new CssLayout();
        frame.addComponent(moduleIcon);
        frame.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component moduleComponent = modulesComponents.get(tabName);
                if (tabs.getTab(moduleComponent) == null) {
                    tabs.addComponent(moduleComponent);
                    tabs.getTab(moduleComponent).setCaption(tabName);
                    tabs.getTab(moduleComponent).setClosable(true);
                    tabs.setSelectedTab(moduleComponent);
                } else {
                    tabs.setSelectedTab(moduleComponent);
                }
            }
        });
        if (ui != null) {
            ui.access(new Runnable() {
                @Override
                public void run() {
                    addComponent(frame);
                }
            });
        } else {
            addComponent(frame);
        }
        modulesComponents.put(tabName, component);
        modulesFrames.put(tabName, frame);
    }

    public void removeExtension(final String tabName) {
        if (ui != null) {
            ui.access(new Runnable() {
                @Override
                public void run() {
                    removeComponent(modulesFrames.get(tabName));
                }
            });
        } else {
            removeComponent(modulesFrames.get(tabName));
        }
    }
}
