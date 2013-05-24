package com.peergreen.webconsole.core.scope;

import com.peergreen.newsfeed.RssService;
import com.peergreen.webconsole.core.api.IScopeFactory;
import com.vaadin.navigator.View;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

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

    /**
     * Rss Service
     */
    @Requires
    RssService rssService;

    /** {@inheritDoc}
     */
    @Override
    public String getName() {
        return SCOPE_NAME;
    }

    /** {@inheritDoc}
     */
    @Override
    public View getView() {
        return new HomeScopeView(rssService);
    }

    /** {@inheritDoc}
     */
    @Override
    public String getStyle() {
        return STYLE;
    }
}
