package com.peergreen.webconsole.core.scopes;

import com.peergreen.newsfeed.RssService;
import com.peergreen.webconsole.core.api.IScopeProvider;
import com.peergreen.webconsole.core.api.IUiManager;
import com.peergreen.webconsole.core.api.IViewContribution;
import com.vaadin.navigator.View;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

/**
 * Home scope provider
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides(specifications = IScopeProvider.class)
public class HomeScope extends AbstractScope implements IScopeProvider {

    public final static String SCOPE_NAME = "home";

    @Requires
    private RssService rssService;

    @Requires
    protected IUiManager uiManager;

    @Override
    public String getName() {
        return SCOPE_NAME;
    }

    @Override
    public View getView() {
        if (firstCall) {
            firstCall = false;
            return view;
        }
        View newView = new HomeScopeView(rssService);
        instancesOfScopeView.add(newView);
        return newView;
    }

    @Bind(id = SCOPE_NAME + "bind", aggregate = true, optional = true)
    public void addView(IViewContribution viewContribution) {
//        views.add(viewContribution);
//        badge++;
//        updateInstancesOfScopeView(viewContribution, false);
//        uiManager.updateScopeMenuButton(getName());
//        uiManager.updateScopeBadge(getName());
    }

    @Unbind(id = SCOPE_NAME + "unbind", aggregate = true, optional = true)
    public void removeView(IViewContribution viewContribution) {
//        views.remove(viewContribution);
//        badge--;
//        updateInstancesOfScopeView(viewContribution, true);
//        uiManager.updateScopeMenuButton(getName());
//        uiManager.updateScopeBadge(getName());
    }

    @Validate
    public void start() {
        view = new HomeScopeView(rssService);
        instancesOfScopeView.add(view);
        firstCall = true;
        uiManager.setMainScope(SCOPE_NAME);
    }

}
