package com.peergreen.webconsole.core.scope;

import com.peergreen.webconsole.module.IModuleFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Default scope view
 * @author Mohammed Boukada
 */
public interface IDefaultScopeTabsView {

    /**
     * Get scope name
     * @return
     */
    String getScopeName();

    /**
     * Add scopeless modules to default scopes
     * @param modules
     */
    void addScopelessModules(ConcurrentLinkedQueue<IModuleFactory> modules);

}