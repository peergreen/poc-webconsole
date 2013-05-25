package com.peergreen.webconsole.core.scope;

import com.peergreen.webconsole.core.api.IScopeFactory;
import com.peergreen.webconsole.core.api.IScopeTabsFactory;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

/**
 * Default scope factory
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides
public class DefaultScope implements IScopeFactory {

    /**
     * Default scope name
     */
    public final static String SCOPE_NAME = "others";

    /**
     * Default scope button style
     */
    final static String STYLE = "icon-dashboard";

    /**
     * Tabs scope view factory
     */
    @Requires
    IScopeTabsFactory scopeTabsFactory;

    /** {@inheritDoc}
     */
    @Override
    public String getName() {
        return SCOPE_NAME;
    }

    /** {@inheritDoc}
     */
    @Override
    public com.vaadin.ui.Component getView() {
        return scopeTabsFactory.createInstance(SCOPE_NAME, true);
    }

    /** {@inheritDoc}
     */
    @Override
    public String getStyle() {
        return STYLE;
    }
}