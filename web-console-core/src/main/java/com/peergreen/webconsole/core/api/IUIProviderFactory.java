package com.peergreen.webconsole.core.api;

import com.vaadin.server.UIProvider;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 22/05/13
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public interface IUIProviderFactory {

    UIProvider createUIProvider(IConsole console);
}
