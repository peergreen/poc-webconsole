package com.peergreen.webconsole.core.api;

import com.vaadin.navigator.View;

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
    View createInstance(String scopeName);

    /**
     * Create an instance of tabs view for default scope
     * @param scopeName
     * @param isDefaultScope
     * @return
     */
    View createInstance(String scopeName, boolean isDefaultScope);

}
