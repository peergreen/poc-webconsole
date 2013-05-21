package com.peergreen.webconsole.scopes.test;

import com.peergreen.webconsole.core.api.IScopeProvider;
import com.peergreen.webconsole.core.api.IViewContribution;
import com.peergreen.webconsole.core.api.IHelpManager;
import com.peergreen.webconsole.core.api.IUiManager;
import com.peergreen.webconsole.core.scopes.AbstractScope;
import com.vaadin.navigator.View;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 19/05/13
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
@Component
@Instantiate
@Provides
public class TestScope extends AbstractScope implements IScopeProvider {

    /**
     * Scope name
     */
    public final static String SCOPE_NAME = "web";

    /**
     * Help Manager
     */
    @Requires
    private IHelpManager helpManager;

    /**
     * UI Manager
     */
    @Requires
    private IUiManager uiManager;

    /**
     * Get scope name
     * @return scope name
     */
    @Override
    public String getName() {
        return SCOPE_NAME;
    }

    /**
     * Get scope view
     * @return instance of scope view
     */
    @Override
    public View getView() {
        if (firstCall) {
            firstCall =  false;
            return view;
        }
        View newView = new ScopeTestView(views, helpManager);
        instancesOfScopeView.add(newView);
        return newView;
    }

    /**
     * Register a new view in this scope
     * @param viewContribution
     */
    public void addView(IViewContribution viewContribution) {
        if (view != null) {
            views.add(viewContribution);
            badge++;
            updateInstancesOfScopeView(viewContribution, false);
            uiManager.updateScopeMenuButton(getName());
            uiManager.updateScopeBadge(getName());
        } else {
            viewsInWaitList.add(viewContribution);
        }
    }

    /**
     * Unregister a view from this scope
     * @param viewContribution
     */
    public void removeView(IViewContribution viewContribution) {
        if (view != null) {
            views.remove(viewContribution);
            badge--;
            updateInstancesOfScopeView(viewContribution, true);
            uiManager.updateScopeMenuButton(getName());
            uiManager.updateScopeBadge(getName());
        } else {
            if (viewsInWaitList.contains(viewContribution)) {
                viewsInWaitList.remove(viewContribution);
            }
        }
    }

    @Bind(aggregate = true, optional = true)
    public void bindViewContribution(IViewContribution view) {
        if (SCOPE_NAME.equals(view.getScope())) {
            try {
                addView(view);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Unbind(aggregate = true, optional = true)
    public void unbindViewContribution(IViewContribution view) {
        if(SCOPE_NAME.equals(view.getScope())) {
            removeView(view);
        }
    }

    @Validate
    public void start() {
        view = new ScopeTestView(views, helpManager);
        instancesOfScopeView.add(view);
        firstCall = true;
        processWaitList();
    }

    @Invalidate
    public void stop() {

    }

    protected void processWaitList() {
        for (IViewContribution view : viewsInWaitList) {
            addView(view);
        }
        viewsInWaitList.clear();
    }
}
