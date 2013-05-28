package com.peergreen.webconsole.core.scope;

import com.peergreen.webconsole.core.api.IDefaultScopeTabsView;
import com.peergreen.webconsole.core.api.IModuleFactory;
import com.peergreen.webconsole.core.api.INotifierService;
import com.peergreen.webconsole.core.api.IScopeFactory;
import com.peergreen.webconsole.core.api.IScopelessModuleCollector;
import com.peergreen.webconsole.core.api.ISecurityManager;
import com.peergreen.webconsole.core.api.UIContext;
import com.peergreen.webconsole.core.exception.ExceptionView;
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
    private ConcurrentHashMap<IModuleFactory, Component> modulesViews = new ConcurrentHashMap<>();

    /**
     * List of scopes
     */
    private List<String> scopes = new ArrayList<>();

    /**
     * Scope name
     */
    private String scopeName;

    /**
     * List of scopes name range
     */
    private List<String> scopesRange;

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
     * Security Manager
     */
    private ISecurityManager securityManager;

    /**
     * Whether this view scope is for default scope
     */
    private boolean isDefaultScope;

    /**
     * Init tabs scope view
     * @param scopeName
     * @param scopesRange
     * @param isDefaultScope
     */
    public ScopeTabsView(String scopeName, List<String> scopesRange, boolean isDefaultScope, UIContext context) {

        this.scopeName = scopeName;
        this.scopesRange = scopesRange;
        this.isDefaultScope = isDefaultScope;
        this.securityManager = context.getSecurityManager();

        setSizeFull();

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
        if (canAddModule(moduleFactory)) {
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
        if (modulesViews.containsKey(moduleFactory)) {
            removeModule(moduleFactory);
            if (modulesViews.isEmpty()) {
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
        // is this a default scope
        if (isDefaultScope && !scopes.equals(scopeFactory.getSymbolicName())) {
            scopes.add(scopeFactory.getSymbolicName());
            // A scope was bound, check if its modules are under the default scope
            for (Map.Entry<IModuleFactory, Component> component : modulesViews.entrySet()) {
                if (scopeFactory.getSymbolicName().equals(component.getKey().getScope())) {
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
        if (!isDefaultScope && securityManager.isUserLogged()) {
            // This scope is unbound, add its modules to scopeless modules collector
            for (Map.Entry<IModuleFactory, Component> component : modulesViews.entrySet()) {
                scopelessModuleCollector.addModule(component.getKey());
            }
            // Notify default scopes that there is scopeless modules
            scopelessModuleCollector.notifyDefaultScopes();
        }
    }

    private boolean canAddModule(IModuleFactory moduleFactory) {
        if (!securityManager.isUserInRoles(moduleFactory.getAllowedRoles())) {
            return false;
        }
        if (isDefaultScope && !scopes.contains(moduleFactory.getScope())) {
            return true;
        } else {
            if (scopesRange.isEmpty()) {
                return scopeName.equals(moduleFactory.getScope());
            } else {
                return scopesRange.contains(moduleFactory.getScope());
            }
        }
    }

    /**
     * Add the module to the scope view
     * @param moduleFactory
     */
    private void addModule(IModuleFactory moduleFactory) {
        if (!modulesViews.containsKey(moduleFactory)) {
            Component view;
            try {
                view = moduleFactory.getView();
            } catch (Exception ex) {
                view = new ExceptionView(ex);
            }
            view.setSizeFull();
            tabs.addComponent(view);
            tabs.getTab(view).setClosable(true);
            tabs.getTab(view).setCaption(moduleFactory.getName());
            modulesViews.put(moduleFactory, view);
        }
    }

    /**
     * Remove the module from the scope view
     * @param moduleFactory
     */
    private void removeModule(IModuleFactory moduleFactory) {
        tabs.removeComponent(modulesViews.get(moduleFactory));
        modulesViews.remove(moduleFactory);
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
