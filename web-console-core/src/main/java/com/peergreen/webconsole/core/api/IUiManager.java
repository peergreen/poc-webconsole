package com.peergreen.webconsole.core.api;

/**
 * UI Manager
 * @author Mohammed Boukada
 */
public interface IUiManager {

    /**
     * Update scope badge
     * @param scopeName
     */
    void updateScopeBadge(String scopeName);

    /**
     * Update scope menu button
     * @param scopeName
     */
    void updateScopeMenuButton(String scopeName);

    /**
     * Set main scope
     * @param scopeName
     */
    void setMainScope(String scopeName);
}
