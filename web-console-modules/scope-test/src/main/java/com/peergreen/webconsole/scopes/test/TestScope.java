package com.peergreen.webconsole.scopes.test;

import com.peergreen.webconsole.Extension;
import com.peergreen.webconsole.ExtensionPoint;
import com.peergreen.webconsole.INotifierService;
import com.peergreen.webconsole.Inject;
import com.peergreen.webconsole.Link;
import com.peergreen.webconsole.Ready;
import com.peergreen.webconsole.Scope;
import com.peergreen.webconsole.Unlink;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

import java.util.Dictionary;

/**
 * @author Mohammed Boukada
 */
@Extension
@ExtensionPoint("com.peergreen.webconsole.pgadmin.scope")
@Scope(name = "test")
public class TestScope extends TabSheet {

    @Inject
    INotifierService notifierService;

    @Ready
    public void buildComponent() {
        setSizeFull();
        setCloseHandler(new TabSheet.CloseHandler() {
            @Override
            public void onTabClose(TabSheet tabsheet, com.vaadin.ui.Component tabContent) {
                notifierService.addNotification("Warning ! You have closed " +
                        tabsheet.getTab(tabContent).getCaption() + " module");
                tabsheet.removeComponent(tabContent);
            }
        });
    }

    @Link("tab")
    public void addTabs(Component tab, Dictionary properties) {
        tab.setSizeFull();
        addTab(tab, (String) properties.get("extension.name")).setClosable(true);
    }

    @Unlink("tab")
    public void removeTabs(Component tab) {
        removeComponent(tab);
    }
}
