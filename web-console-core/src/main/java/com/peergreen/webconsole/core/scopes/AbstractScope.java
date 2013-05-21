package com.peergreen.webconsole.core.scopes;

import com.peergreen.webconsole.core.api.IScopeView;
import com.peergreen.webconsole.core.api.IViewContribution;
import com.vaadin.navigator.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract scope
 * @author Mohammed Boukada
 */
public class AbstractScope {

    /**
     * Scope badge
     */
    protected int badge = 0;

    /**
     * First instance of scope view
     */
    protected View view;

    /**
     * List of views in scope
     */
    protected List<IViewContribution> views = new ArrayList<>();

    /**
     * Instances of scope view
     */
    protected List<View> instancesOfScopeView = new ArrayList<>();

    /**
     * List of views bound before scope's ipojo component instantiation
     */
    protected List<IViewContribution> viewsInWaitList = new ArrayList<>();

    /**
     * To define the first instantiation of an UI
     */
    protected boolean firstCall;

    public List<IViewContribution> getViewsInScope() {
        return views;
    }

    /**
     * Update instances of scope view when changes affect this scope
     * @param viewContribution
     * @param remove
     */
    protected void updateInstancesOfScopeView(IViewContribution viewContribution, boolean remove) {
        List<View> viewsToRemove = new ArrayList<>();
        for (View view : instancesOfScopeView) {
            if (view != null) {
                if (remove) {
                    ((IScopeView) view).removeView(viewContribution);
                } else {
                    ((IScopeView) view).addView(viewContribution);
                }
            } else {
                viewsToRemove.add(view);
            }
        }

        for (View view : viewsToRemove) {
            instancesOfScopeView.remove(view);
        }
    }

    /**
     * Get badge
     * @return badge
     */
    public int getBadge() {
        return badge;
    }

    /**
     * Set badge
     * @param badge
     */
    public void setBadge(int badge) {
        this.badge = badge;
    }
}
