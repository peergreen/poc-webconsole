package com.peergreen.webconsole.modules.module1;

import com.peergreen.webconsole.Extension;
import com.peergreen.webconsole.ExtensionPoint;
import com.peergreen.webconsole.Ready;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 14/05/13
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
@Extension
@ExtensionPoint("com.peergreen.webconsole.modules.module1.StoreScope.tab")
public class Module1 extends CssLayout {

    @Ready
    public void createView() {
        addStyleName("timeline");
        Label header = new Label("This is a view contribution ! ");
        header.addStyleName("h1");
        addComponent(header);
    }
}
