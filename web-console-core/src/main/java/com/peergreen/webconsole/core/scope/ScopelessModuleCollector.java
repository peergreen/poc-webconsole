package com.peergreen.webconsole.core.scope;

import com.peergreen.webconsole.core.api.IDefaultScopeTabsView;
import com.peergreen.webconsole.core.api.IModuleFactory;
import com.peergreen.webconsole.core.api.IScopelessModuleCollector;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Scopeless modules collector
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides
public class ScopelessModuleCollector implements IScopelessModuleCollector {

    /**
     * Scopeless modules
     */
    private ConcurrentLinkedQueue<IModuleFactory> modules = new ConcurrentLinkedQueue<>();

    /**
     * Default scopes view
     */
    private ConcurrentLinkedQueue<IDefaultScopeTabsView> defaultScopes = new ConcurrentLinkedQueue<>();

    /** {@inheritDoc}
     */
    @Override
    public void addModule(IModuleFactory moduleFactory) {
        if(!modules.contains(moduleFactory)){
            modules.add(moduleFactory);
        }
    }

    /** {@inheritDoc}
     */
    @Override
    public void removeModule(IModuleFactory moduleFactory) {
        if (modules.contains(moduleFactory)){
            modules.remove(moduleFactory);
        }
    }

    /** {@inheritDoc}
     */
    @Override
    public void notifyDefaultScopes() {
        for (IDefaultScopeTabsView defaultScope : defaultScopes) {
            defaultScope.addScopelessModules(modules);
        }
    }

    /**
     * Bind default scopes
     * @param defaultScopeTabsView
     */
    @Bind(aggregate = true, optional = true)
    public void bindDefaultScope(IDefaultScopeTabsView defaultScopeTabsView) {
        if (DefaultScope.SCOPE_NAME.equals(defaultScopeTabsView.getScopeName())) {
            defaultScopes.add(defaultScopeTabsView);
        }
    }

    /**
     * Unbind default scopes
     * @param defaultScopeTabsView
     */
    @Unbind
    public void unbindDefaultScope(IDefaultScopeTabsView defaultScopeTabsView) {
        if (defaultScopes.contains(defaultScopeTabsView)) {
            defaultScopes.remove(defaultScopeTabsView);
        }
    }
}
