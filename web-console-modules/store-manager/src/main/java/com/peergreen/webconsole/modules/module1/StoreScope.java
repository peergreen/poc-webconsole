package com.peergreen.webconsole.modules.module1;

import com.peergreen.webconsole.Extension;
import com.peergreen.webconsole.ExtensionPoint;
import com.peergreen.webconsole.ISecurityManager;
import com.peergreen.webconsole.Inject;
import com.peergreen.webconsole.Link;
import com.peergreen.webconsole.Ready;
import com.peergreen.webconsole.Scope;
import com.peergreen.webconsole.Unlink;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

import javax.annotation.security.RolesAllowed;
import java.util.Dictionary;

/**
 * @author Mohammed Boukada
 */
@Extension
@ExtensionPoint("com.peergreen.webconsole.pgadmin.scope")
@Scope("Store")
public class StoreScope extends TabSheet {

    @Inject
    ISecurityManager securityManager;

    @Ready
    public void createView() {
        setSizeFull();
        securityManager.getUserName();
    }

    @Link("tab")
    public void addTabs(Component tab, Dictionary properties) {
        tab.setSizeFull();
        addTab(tab, (String) properties.get("tab.name"));
    }

    @Unlink("tab")
    public void removeTabs(Component tab) {
        removeComponent(tab);
    }
}
