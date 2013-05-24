package com.peergreen.webconsole.core.scope;

import com.peergreen.webconsole.core.api.IDefaultScopeTabsView;
import com.peergreen.webconsole.core.api.IModuleFactory;
import com.peergreen.webconsole.core.api.INotifierService;
import com.peergreen.webconsole.core.api.IScopeFactory;
import com.peergreen.webconsole.core.api.IScopelessModuleCollector;
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
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 22/05/13
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
@org.apache.felix.ipojo.annotations.Component
@Provides(specifications = {ScopeTabsView.class, IDefaultScopeTabsView.class})
public class ScopeTabsView extends CssLayout implements View, IDefaultScopeTabsView {

    /*
        List of modules tabs in this scope
     */
    private TabSheet tabs;

    /*
        Map of modules and there views registered in tabs
        Used for identify a tab
     */
    private ConcurrentHashMap<IModuleFactory, Component> components;

    private List<String> scopes;

    private String scopeName;

    @Requires
    private INotifierService notifierService;

    @Requires
    private IScopelessModuleCollector scopelessModuleCollector;

    private boolean isDefaultScope;

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

    @Bind(aggregate = true, optional = true)
    public void bindModule(IModuleFactory moduleFactory) {
        if (scopeName.equals(moduleFactory.getScope()) ||
                (isDefaultScope && !scopes.contains(moduleFactory.getScope()))) {
            addModule(moduleFactory);
            scopelessModuleCollector.removeModule(moduleFactory);
            notifierService.incrementBadge(this);
        }
    }

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

    @Bind(aggregate = true, optional = true)
    public void bindScope(IScopeFactory scopeFactory) {
        if (isDefaultScope && !scopeName.equals(scopeFactory.getName())) {
            scopes.add(scopeFactory.getName());
            for (Map.Entry<IModuleFactory, Component> component : components.entrySet()) {
                if (scopeFactory.getName().equals(component.getKey().getScope())) {
                    removeModule(component.getKey());
                }
            }
        }
    }

//    @Unbind(aggregate = true, optional = true)
//    public void unbindScope(IScopeFactory scopeFactory) {
//        if (isDefaultScope && !scopeName.equals(scopeFactory.getName())) {
//            scopes.remove(scopeFactory.getName());
//            for (IModuleFactory moduleFactory : scopelessModuleCollector.getModules()) {
//                addModule(moduleFactory);
//            }
//        }
//    }

    @Invalidate
    public void stop() {
        if (!DefaultScope.SCOPE_NAME.equals(scopeName)) {
            for (Map.Entry<IModuleFactory, Component> component : components.entrySet()) {
                scopelessModuleCollector.addModule(component.getKey());
            }
            scopelessModuleCollector.notifyDefaultScopes();
        } else {
            System.out.print("");
        }
    }

    public void addModule(IModuleFactory moduleFactory) {
        if (!components.containsKey(moduleFactory)) {
            Component view = moduleFactory.getView();
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

    public void removeModule(IModuleFactory moduleFactory) {
        //tabs.getUI().getSession().getLockInstance().lock();
        try {
            tabs.removeComponent(components.get(moduleFactory));
        } finally {
          //  tabs.getUI().getSession().getLockInstance().unlock();
        }
        components.remove(moduleFactory);
    }

    public String getScopeName() {
        return scopeName;
    }

    @Override
    public void addScopelessModules(ConcurrentLinkedQueue<IModuleFactory> modules) {
        for (IModuleFactory moduleFactory : modules) {
            if (scopes.contains(moduleFactory.getScope())) {
                scopes.remove(moduleFactory.getScope());
            }
            addModule(moduleFactory);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
