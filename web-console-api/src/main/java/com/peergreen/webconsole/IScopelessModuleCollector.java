package com.peergreen.webconsole;

import com.peergreen.webconsole.module.IModuleFactory;

/**
 * Scopeless modules collector
 * @author Mohammed Boukada
 */
public interface IScopelessModuleCollector {

    /**
     * Add a scopeless module
     * @param moduleFactory
     */
    void addModule(IModuleFactory moduleFactory);

    /**
     * A module has found his scope !
     * It is no longer scopeless
     * @param moduleFactory
     */
    void removeModule(IModuleFactory moduleFactory);

    /**
     * Notify default scopes that there is scopeless modules
     */
    void notifyDefaultScopes();
}
