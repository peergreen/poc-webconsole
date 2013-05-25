package com.peergreen.webconsole.core.api;

import com.vaadin.ui.Component;

/**
 * Tabs scope factory
 * @author Mohammed Boukada
 */
public interface IScopeTabsFactory {

    /**
     * Create an instance of tabs view
     * @param scopeName
     * @return
     */
    Component createInstance(String scopeName);

    /**
     * Create an instance of tabs view for default scope
     * @param scopeName
     * @param isDefaultScope
     * @return
     */
    Component createInstance(String scopeName, boolean isDefaultScope);

}
