package com.peergreen.webconsole.core.scope;

import com.peergreen.newsfeed.RssService;
import com.peergreen.webconsole.core.api.IScopeFactory;
import com.vaadin.navigator.View;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

/**
 * Home scope provider
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides
public class HomeScope implements IScopeFactory {

    public final static String SCOPE_NAME = "home";

    final static String STYLE = "icon-dashboard";

    @Requires
    RssService rssService;

    @Override
    public String getName() {
        return SCOPE_NAME;
    }

    @Override
    public View getView() {
        return new HomeScopeView(rssService);
    }

    @Override
    public String getStyle() {
        return STYLE;
    }
}
