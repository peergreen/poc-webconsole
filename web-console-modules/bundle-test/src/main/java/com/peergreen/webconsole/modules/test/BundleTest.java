package com.peergreen.webconsole.modules.test;

import com.peergreen.webconsole.Extension;
import com.peergreen.webconsole.ExtensionPoint;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.apache.felix.ipojo.annotations.Validate;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 14/05/13
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
@Extension
@ExtensionPoint("com.peergreen.webconsole.scopes.test.TestScope.tab")
public class BundleTest extends CssLayout {

    @Validate
    public void createView() {
        addStyleName("timeline");
        Label header = new Label("This is a view contribution ! ");
        header.addStyleName("h1");
        addComponent(header);
    }
}
