package com.peergreen.webconsole.core.scopes;

import com.peergreen.webconsole.core.api.IHelpManager;
import com.peergreen.webconsole.core.api.IScopeView;
import com.peergreen.webconsole.core.api.IViewContribution;
import com.peergreen.webconsole.core.vaadin7.HelpOverlay;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;

import java.util.HashMap;
import java.util.List;

/**
 * Default scope view
 * @author Mohammed Boukada
 */
public class DefaultScopeView extends CssLayout implements View, IScopeView {

    /*
        List of modules tabs in this scope
     */
    private TabSheet tabs;

    /*
        Map of modules and there views registered in tabs
        Used for identify a tab
     */
    private HashMap<IViewContribution, Component> components;

    public DefaultScopeView(List<IViewContribution> views, final IHelpManager helpManager) {

        setSizeFull();
        tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName("borderless");
        addComponent(tabs);

        components = new HashMap<>();

        for (IViewContribution module : views) {
            Component view = module.getView();
            tabs.addComponent(view);
            tabs.getTab(view).setClosable(true);
            tabs.getTab(view).setCaption(module.getName());
            components.put(module, view);
        }

        tabs.setCloseHandler(new TabSheet.CloseHandler() {
            @Override
            public void onTabClose(TabSheet tabsheet, Component tabContent) {
                HelpOverlay w = helpManager.addOverlay("Attention",
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

    public void addView(IViewContribution viewContribution) {
        Component view = viewContribution.getView();
        tabs.addComponent(view);
        tabs.getTab(view).setClosable(true);
        tabs.getTab(view).setCaption(viewContribution.getName());
        components.put(viewContribution, view);
    }

    public void removeView(IViewContribution viewContribution) {
        tabs.removeComponent(components.get(viewContribution));
        components.remove(viewContribution);
    }
}
