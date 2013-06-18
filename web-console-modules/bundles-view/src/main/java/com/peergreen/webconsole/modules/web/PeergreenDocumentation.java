package com.peergreen.webconsole.modules.web;

import com.peergreen.webconsole.Extension;
import com.peergreen.webconsole.ExtensionPoint;
import com.peergreen.webconsole.Ready;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.VerticalLayout;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 16/05/13
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
 */

@Extension
@ExtensionPoint("com.peergreen.webconsole.scopes.test.TestScope.tab")
public class PeergreenDocumentation extends VerticalLayout {

    @Ready
    public void createView() {
        setMargin(true);
        setSpacing(true);
        BrowserFrame browser = new BrowserFrame("",
                new ExternalResource("http://docs.peergreen.com/peergreen_server/latest/reference/xhtml-single/user-guide.xhtml"));
        browser.setSizeFull();
        addComponent(browser);
        setExpandRatio(browser, 1.5f);
    }
}
