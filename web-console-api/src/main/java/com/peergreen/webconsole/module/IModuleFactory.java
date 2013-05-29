package com.peergreen.webconsole.module;

import com.vaadin.ui.Component;

import java.util.List;

/**
 * Vaadin module factory
 * @author Mohammed Boukada
 */
public interface IModuleFactory {

    /**
     * Get module view
     * @return vaadin component
     */
    public Component getView();

    /**
     * Get scope name
     * @return scope name
     */
    public String getScope();

    /**
     * Get module name
     * @return module name
     */
    public String getName();

    /**
     * Get allowed roles
     * @return allowed roles
     */
    List<String> getAllowedRoles();
}
