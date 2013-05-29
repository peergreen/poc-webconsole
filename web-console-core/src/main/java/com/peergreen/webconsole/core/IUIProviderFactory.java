package com.peergreen.webconsole.core;

import com.peergreen.webconsole.IConsole;
import com.vaadin.server.UIProvider;

/**
 * Vaadin UI provider factory
 * @author Mohammed Boukada
 */
public interface IUIProviderFactory {

    /**
     * Create an UI provider for a console
     * @param console
     * @return
     */
    UIProvider createUIProvider(IConsole console);
}
