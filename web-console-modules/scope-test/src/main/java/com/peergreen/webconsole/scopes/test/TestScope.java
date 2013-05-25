package com.peergreen.webconsole.scopes.test;

import com.peergreen.webconsole.core.api.IModuleFactory;
import com.peergreen.webconsole.core.api.INotifierService;
import com.peergreen.webconsole.core.api.IScopeFactory;
import com.peergreen.webconsole.core.api.IScopeTabsFactory;
import com.vaadin.navigator.View;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

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
public class TestScope implements IScopeFactory {

    final static String SCOPE_NAME = "web";

    final static String STYLE = "icon-sales";

    @Requires
    IScopeTabsFactory scopeTabsFactory;

    @Override
    public String getName() {
        return SCOPE_NAME;
    }

    @Override
    public com.vaadin.ui.Component getView() {
        return scopeTabsFactory.createInstance(SCOPE_NAME);
    }

    @Override
    public String getStyle() {
        return STYLE;
    }
}
