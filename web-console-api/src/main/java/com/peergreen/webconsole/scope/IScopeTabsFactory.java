package com.peergreen.webconsole.scope;

import com.peergreen.webconsole.UIContext;
import com.vaadin.ui.Component;

import java.util.List;

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
    Component createInstance(String scopeName, UIContext context);

    /**
     * Create an instance of tabs view
     * @param scopeName
     * @param scopesRange
     * @return
     */
    Component createInstance(String scopeName, List<String> scopesRange, UIContext context);

    /**
     * Create an instance of tabs view
     * @param scopeName
     * @param isDefaultScope
     * @return
     */
    Component createInstance(String scopeName, boolean isDefaultScope, UIContext context);

    /**
     * Create an instance of tabs view for default scope
     * @param scopeName
     * @param scopesRange
     * @param isDefaultScope
     * @return
     */
    Component createInstance(String scopeName, List<String> scopesRange, boolean isDefaultScope, UIContext context);

}
