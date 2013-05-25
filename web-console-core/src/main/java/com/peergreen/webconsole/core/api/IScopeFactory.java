package com.peergreen.webconsole.core.api;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;

import java.util.List;

/**
 * Scope factory
 * @author Mohammed Boukada
 */
public interface IScopeFactory {

    /**
     * Get scope name
     * @return scope name
     */
    String getName();

    /**
     * Get scope view
     * @return instance of scope view
     */
    Component getView();

    /**
     * Get scope button style
     * @return scope button style
     */
    String getStyle();
}
