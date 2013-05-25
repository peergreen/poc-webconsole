package com.peergreen.webconsole.core.scope;

import com.peergreen.webconsole.core.api.IDefaultScopeTabsView;
import com.peergreen.webconsole.core.api.IModuleFactory;
import com.peergreen.webconsole.core.api.INotifierService;
import com.peergreen.webconsole.core.api.IScopeFactory;
import com.peergreen.webconsole.core.api.IScopelessModuleCollector;
import com.peergreen.webconsole.core.exception.ExceptionView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Tabs scope view
 * @author Mohammed Boukada
 */
@org.apache.felix.ipojo.annotations.Component
@Provides(specifications = {ScopeTabsView.class, IDefaultScopeTabsView.class})
public class ScopeTabsView extends CssLayout implements IDefaultScopeTabsView {

    /*
        List of modules tabs in this scope
     */
    private TabSheet tabs;

    /*
        Map of modules and there views registered in tabs
        Used for identify a tab
     */
    private ConcurrentHashMap<IModuleFactory, Component> components;

    /**
     * List of scopes
     */
    private List<String> scopes;

    /**
     * Scope name
     */
    private String scopeName;

    /**
     * Notifier service
     */
    @Requires
    private INotifierService notifierService;

    /**
     * Scopeless modules collector
     */
    @Requires
    private IScopelessModuleCollector scopelessModuleCollector;

    /**
     * Whether this view scope is for default scope
     */
    private boolean isDefaultScope;

    /**
     * Init tabs scope view
     * @param scopeName
     * @param isDefaultScope
     */
    public ScopeTabsView(String scopeName, boolean isDefaultScope) {

        this.scopeName = scopeName;
        this.isDefaultScope = isDefaultScope;

        setSizeFull();

        components = new ConcurrentHashMap<>();
        scopes = new ArrayList<>();
        tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName("borderless");
        addComponent(tabs);

        tabs.setCloseHandler(new TabSheet.CloseHandler() {
            @Override
            public void onTabClose(TabSheet tabsheet, Component tabContent) {
                notifierService.addNotification("Attention ! You have closed " +
                        tabsheet.getTab(tabContent).getCaption() + " module");
                tabsheet.removeComponent(tabContent);
            }
        });
    }

    /**
     * Bind a new module
     * @param moduleFactory
     */
    @Bind(aggregate = true, optional = true)
    public void bindModule(IModuleFactory moduleFactory) {
        if (scopeName.equals(moduleFactory.getScope()) ||
                (isDefaultScope && !scopes.contains(moduleFactory.getScope()))) {
            addModule(moduleFactory);
            scopelessModuleCollector.removeModule(moduleFactory);
            notifierService.incrementBadge(this);
        }
    }

    /**
     * Unbind a module
     * @param moduleFactory
     */
    @Unbind
    public void unbindModule(IModuleFactory moduleFactory) {
        if (components.containsKey(moduleFactory)) {
            removeModule(moduleFactory);
            if (components.isEmpty()) {
                notifierService.hideScopeButton(this);
            } else {
                notifierService.decrementBadge(this);
            }
        }
    }

    /**
     * Bind a new scope
     * @param scopeFactory
     */
    @Bind(aggregate = true, optional = true)
    public void bindScope(IScopeFactory scopeFactory) {
        // is this view is for a default scope
        if (isDefaultScope && !scopeName.equals(scopeFactory.getName())) {
            scopes.add(scopeFactory.getName());
            // A scope was bound, check if its modules are under the default scope
            for (Map.Entry<IModuleFactory, Component> component : components.entrySet()) {
                if (scopeFactory.getName().equals(component.getKey().getScope())) {
                    // Remove the module because its scope was bound
                    removeModule(component.getKey());
                }
            }
        }
    }

    /**
     * Stop the ipojo component
     */
    @Invalidate
    public void stop() {
        // is this views not for a default scope
        if (!DefaultScope.SCOPE_NAME.equals(scopeName)) {
            // This scope is unbound, add its modules to scopeless modules collector
            for (Map.Entry<IModuleFactory, Component> component : components.entrySet()) {
                scopelessModuleCollector.addModule(component.getKey());
            }
            // Notify default scopes that there is scopeless modules
            scopelessModuleCollector.notifyDefaultScopes();
        }
    }

    /**
     * Add the module to the scope view
     * @param moduleFactory
     */
    private void addModule(IModuleFactory moduleFactory) {
        if (!components.containsKey(moduleFactory)) {
            Component view;
            try {
                view = moduleFactory.getView();
            } catch (Exception ex) {
                view = new ExceptionView(ex);
            }
            view.setSizeFull();
            //tabs.getUI().getSession().getLockInstance().lock();
            try {
                tabs.addComponent(view);
            } finally {
              //  tabs.getUI().getSession().getLockInstance().unlock();
            }
            tabs.getTab(view).setClosable(true);
            tabs.getTab(view).setCaption(moduleFactory.getName());
            components.put(moduleFactory, view);
        }
    }

    /**
     * Remove the module from the scope view
     * @param moduleFactory
     */
    private void removeModule(IModuleFactory moduleFactory) {
        //tabs.getUI().getSession().getLockInstance().lock();
        try {
            tabs.removeComponent(components.get(moduleFactory));
        } finally {
          //  tabs.getUI().getSession().getLockInstance().unlock();
        }
        components.remove(moduleFactory);
    }

    /** {@inheritDoc}
     */
    public String getScopeName() {
        return scopeName;
    }

    /** {@inheritDoc}
     */
    @Override
    public void addScopelessModules(ConcurrentLinkedQueue<IModuleFactory> modules) {
        for (IModuleFactory moduleFactory : modules) {
            if (scopes.contains(moduleFactory.getScope())) {
                scopes.remove(moduleFactory.getScope());
            }
            addModule(moduleFactory);
        }
    }
}
