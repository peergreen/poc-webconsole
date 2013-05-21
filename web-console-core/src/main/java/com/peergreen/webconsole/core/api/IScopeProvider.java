package com.peergreen.webconsole.core.api;

import com.vaadin.navigator.View;

import java.util.List;

/**
 * Scope provider
 * @author Mohammed Boukada
 */
public interface IScopeProvider {

    /**
     * Get scope name
     * @return scope name
     */
    String getName();

    /**
     * Get scope view
     * @return instance of scope view
     */
    View getView();

    /**
     * Get views in the scope
     * @return list of views
     */
    List<IViewContribution> getViewsInScope();

    /**
     * Get badge value
     * @return badge value
     */
    int getBadge();

    /**
     * Set badge value
     * @param badge
     */
    void setBadge(int badge);

    /**
     * Add a view to the scope
     * @param view
     */
    void addView(IViewContribution view);

    /**
     * Remove a view from the scope
     * @param view
     */
    void removeView(IViewContribution view);
}
