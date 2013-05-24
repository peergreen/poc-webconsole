package com.peergreen.webconsole.core.scope;

import com.peergreen.webconsole.core.api.IScopeFactory;
import com.peergreen.webconsole.core.api.IScopeTabsFactory;
import com.vaadin.navigator.View;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

/**
 * Default scope provider
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides
public class DefaultScope implements IScopeFactory {

    public final static String SCOPE_NAME = "others";

    final static String STYLE = "icon-dashboard";

    @Requires
    IScopeTabsFactory scopeTabsFactory;

    @Override
    public String getName() {
        return SCOPE_NAME;
    }

    @Override
    public View getView() {
        return scopeTabsFactory.createInstance(SCOPE_NAME, true);
    }

    @Override
    public String getStyle() {
        return STYLE;
    }
}