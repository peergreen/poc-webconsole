package com.peergreen.webconsole.scopes.test;

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

    private List<String> allowedRoles = new ArrayList<String>() {{
        add("platform-admin");
    }};

    @Requires
    IScopeTabsFactory scopeTabsFactory;

    @Override
    public String getSymbolicName() {
        return SCOPE_NAME;
    }

    @Override
    public List<String> getAllowedRoles() {
        return allowedRoles;
    }

    @Override
    public com.vaadin.ui.Component getView(UIContext context) {
        return scopeTabsFactory.createInstance(SCOPE_NAME, context);
    }

    @Override
    public String getStyle() {
        return STYLE;
    }
}
