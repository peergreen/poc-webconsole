package com.peergreen.webconsole.core.scope;

import com.peergreen.newsfeed.RssService;
import com.peergreen.webconsole.core.api.IScopeFactory;
import com.peergreen.webconsole.core.api.UIContext;
import com.vaadin.navigator.View;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import java.util.ArrayList;
import java.util.List;

/**
 * Home scope factory
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides
public class HomeScope implements IScopeFactory {

    /**
     * Home scope name
     */
    public final static String SCOPE_NAME = "home";

    /**
     * Home scope button style
     */
    final static String STYLE = "icon-dashboard";

    private List<String> allowedRoles = new ArrayList<String>() {{
        add("all");
    }};

    /**
     * Rss Service
     */
    @Requires
    RssService rssService;

    /** {@inheritDoc}
     */
    @Override
    public String getSymbolicName() {
        return SCOPE_NAME;
    }

    @Override
    public List<String> getAllowedRoles() {
        return allowedRoles;
    }

    /** {@inheritDoc}
     */
    @Override
    public com.vaadin.ui.Component getView(UIContext context) {
        return new HomeScopeView(rssService);
    }

    /** {@inheritDoc}
     */
    @Override
    public String getStyle() {
        return STYLE;
    }
}
