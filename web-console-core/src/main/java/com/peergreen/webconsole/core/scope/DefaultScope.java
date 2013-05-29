package com.peergreen.webconsole.core.scope;

import com.peergreen.webconsole.scope.IScopeFactory;
import com.peergreen.webconsole.scope.IScopeTabsFactory;
import com.peergreen.webconsole.UIContext;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> scopesRange = new ArrayList<String>() {{
        add(SCOPE_NAME);
    }};

    private List<String> allowedRoles = new ArrayList<String>() {{
        add("all");
    }};

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
    public String getSymbolicName() {
        return SCOPE_NAME;
    }

    /** {@inheritDoc}
     */
    @Override
    public List<String> getAllowedRoles() {
        return allowedRoles;
    }

    /** {@inheritDoc}
     */
    @Override
    public com.vaadin.ui.Component getView(UIContext context) {
        return scopeTabsFactory.createInstance(SCOPE_NAME, allowedRoles, true, context);
    }

    /** {@inheritDoc}
     */
    @Override
    public String getStyle() {
        return STYLE;
    }
}