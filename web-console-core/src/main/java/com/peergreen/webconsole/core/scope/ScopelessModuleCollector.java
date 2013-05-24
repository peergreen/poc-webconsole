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
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 23/05/13
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
@Component
@Instantiate
@Provides
public class ScopelessModuleCollector implements IScopelessModuleCollector {

    private ConcurrentLinkedQueue<IModuleFactory> modules = new ConcurrentLinkedQueue<>();

    private ConcurrentLinkedQueue<IDefaultScopeTabsView> defaultScopes = new ConcurrentLinkedQueue<>();

    @Override
    public void addModule(IModuleFactory moduleFactory) {
        if(!modules.contains(moduleFactory)){
            modules.add(moduleFactory);
        }
    }

    @Override
    public void removeModule(IModuleFactory moduleFactory) {
        if (modules.contains(moduleFactory)){
            modules.remove(moduleFactory);
        }
    }

    @Override
    public void notifyDefaultScopes() {
        for (IDefaultScopeTabsView defaultScope : defaultScopes) {
            defaultScope.addScopelessModules(modules);
        }
    }

    @Bind(aggregate = true, optional = true)
    public void bindDefaultScope(IDefaultScopeTabsView defaultScopeTabsView) {
        if (DefaultScope.SCOPE_NAME.equals(defaultScopeTabsView.getScopeName())) {
            defaultScopes.add(defaultScopeTabsView);
        }
    }

    @Unbind
    public void unbindDefaultScope(IDefaultScopeTabsView defaultScopeTabsView) {
        if (defaultScopes.contains(defaultScopeTabsView)) {
            defaultScopes.remove(defaultScopeTabsView);
        }
    }
}
