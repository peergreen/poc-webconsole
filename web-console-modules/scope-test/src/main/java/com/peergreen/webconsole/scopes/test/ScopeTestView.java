package com.peergreen.webconsole.scopes.test;

import com.peergreen.webconsole.core.api.IModuleFactory;
import com.peergreen.webconsole.core.api.INotifierService;
import com.peergreen.webconsole.core.notifier.NotificationOverlay;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 19/05/13
 * Time: 15:50
 * To change this template use File | Settings | File Templates.
 */
public class ScopeTestView extends CssLayout implements View {
    /*
        List of modules tabs in this scope
     */
    private TabSheet tabs;

    /*
        Map of modules and there views registered in tabs
        Used for identify a tab
     */
    private HashMap<IModuleFactory, Component> components;

    public ScopeTestView(List<IModuleFactory> views, final INotifierService helpManager) {

        setSizeFull();
        tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName("borderless");
        addComponent(tabs);

        components = new HashMap<>();

        for (IModuleFactory module : views) {
            Component view = module.getView();
            tabs.addComponent(view);
            tabs.getTab(view).setClosable(true);
            tabs.getTab(view).setCaption(module.getName());
            components.put(module, view);
        }

        tabs.setCloseHandler(new TabSheet.CloseHandler() {
            @Override
            public void onTabClose(TabSheet tabsheet, Component tabContent) {
                NotificationOverlay w = helpManager.addOverlay("Attention",
                        "You have closed " + tabsheet.getTab(tabContent).getCaption() + " module",
                        "login");
                w.center();
                getUI().addWindow(w);
                tabsheet.removeComponent(tabContent);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public void addModule(IModuleFactory moduleFactory) {
        Component view = moduleFactory.getView();
        tabs.addComponent(view);
        tabs.getTab(view).setClosable(true);
        tabs.getTab(view).setCaption(moduleFactory.getName());
        components.put(moduleFactory, view);
    }

    public void removeModule(IModuleFactory moduleFactory) {
        tabs.removeComponent(components.get(moduleFactory));
        components.remove(moduleFactory);
    }
}
