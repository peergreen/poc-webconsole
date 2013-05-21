package com.peergreen.webconsole.core.scopes;

import com.peergreen.webconsole.core.api.IHelpManager;
import com.peergreen.webconsole.core.api.IScopeProvider;
import com.peergreen.webconsole.core.api.IUiManager;
import com.peergreen.webconsole.core.api.IViewContribution;
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
import java.util.HashMap;
import java.util.List;

/**
 * Default scope provider
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides(specifications = IScopeProvider.class)
public class DefaultScope extends AbstractScope implements IScopeProvider {

    /**
     * Scope name
     */
    public final static String SCOPE_NAME = "others";

    /**
     * List of scopes in the UI
     */
    private HashMap<String, IScopeProvider> scopes = new HashMap<>();

    /**
     * Help Manager
     */
    @Requires
    private IHelpManager helpManager;

    /**
     * UI Manager
     */
    @Requires
    protected IUiManager uiManager;

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
        View newView = new DefaultScopeView(views, helpManager);
        instancesOfScopeView.add(newView);
        return newView;
    }

    /**
     * Register a new view in this scope
     * @param viewContribution
     */
    public void addView(IViewContribution viewContribution) {
        if (!scopes.containsKey(viewContribution.getScope())) {
            views.add(viewContribution);
            badge++;
            updateInstancesOfScopeView(viewContribution, false);
            uiManager.updateScopeMenuButton(getName());
            uiManager.updateScopeBadge(getName());
        }
    }

    /**
     * Unregister a view from this scope
     * @param viewContribution
     */
    public void removeView(IViewContribution viewContribution) {
        if (views.contains(viewContribution)) {
            views.remove(viewContribution);
            badge--;
            updateInstancesOfScopeView(viewContribution, true);
            uiManager.updateScopeMenuButton(getName());
            uiManager.updateScopeBadge(getName());
        }
    }

    @Bind(aggregate = true, optional = true)
    public void bindViewContribution(IViewContribution view) {
        addView(view);
    }

    @Unbind(aggregate = true, optional = true)
    public void unbindViewContribution(IViewContribution view) {
        removeView(view);
    }

    /**
     * Register a new scope
     * @param scope
     */
    @Bind(aggregate = true, optional = true)
    public void bindScopeProvider(IScopeProvider scope) {
        if (!SCOPE_NAME.equals(scope.getName())) {
            scopes.put(scope.getName(), scope);

            List<IViewContribution> viewsToRemove = new ArrayList<>();
            for (IViewContribution view : views) {
                if (scope.getName().equals(view.getScope())) {
                    viewsToRemove.add(view);
                }
            }

            for (IViewContribution view : viewsToRemove) {
                removeView(view);
            }
        }
    }

    /**
     * Unregister a scope
     * @param scope
     */
    @Unbind(aggregate = true, optional = true)
    public void unbindScopeProvider(IScopeProvider scope) {
        if (!SCOPE_NAME.equals(scope.getName())) {
            scopes.remove(scope.getName());
            for (IViewContribution view : scope.getViewsInScope()) {
                addView(view);
            }
        }
    }

    @Validate
    public void start() {
        view = new DefaultScopeView(views, helpManager);
        instancesOfScopeView.add(view);
        firstCall = true;
    }

    @Invalidate
    public void stop() {

    }
}