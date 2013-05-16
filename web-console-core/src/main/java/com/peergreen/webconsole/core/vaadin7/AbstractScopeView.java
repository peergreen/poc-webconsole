package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.core.api.IViewContribution;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 14/05/13
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */
public class AbstractScopeView extends CssLayout implements View {

    /*
        List of modules tabs in this scope
     */
    private TabSheet tabs;

    /*
        Map of modules and there views registered in tabs
        Used for identify a tab
     */
    private HashMap<IViewContribution, Component> components;

    public AbstractScopeView(List<IViewContribution> modules, final MainUI mainUi) {

        setSizeFull();
        tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName("borderless");
        addComponent(tabs);

        components = new HashMap<>();

        for (IViewContribution module : modules) {
            Component view = module.getView();
            tabs.addComponent(view);
            tabs.getTab(view).setClosable(true);
            tabs.getTab(view).setCaption(module.getName());
            components.put(module, view);
        }

        tabs.setCloseHandler(new TabSheet.CloseHandler() {
            @Override
            public void onTabClose(TabSheet tabsheet, Component tabContent) {
                HelpOverlay w = mainUi.getHelpManager().addOverlay("Attention",
                        "You have closed " + tabsheet.getTab(tabContent).getCaption() + " module",
                        "login");
                w.center();
                mainUi.addWindow(w);
                tabsheet.removeComponent(tabContent);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public void addView(IViewContribution module) {
        Component view = module.getView();
        tabs.addComponent(view);
        tabs.getTab(view).setClosable(true);
        tabs.getTab(view).setCaption(module.getName());
        components.put(module, view);
    }

    public void removeView(IViewContribution module) {
        tabs.removeComponent(components.get(module));
        components.remove(module);
    }
}