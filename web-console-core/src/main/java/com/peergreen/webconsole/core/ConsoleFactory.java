package com.peergreen.webconsole.core;

import javax.servlet.ServletException;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.peergreen.webconsole.IConsole;
import com.peergreen.webconsole.core.osgi.VaadinOSGiServlet;
import com.vaadin.server.UIProvider;

/**
 * Console factory
 * @author Mohammed Boukada
 */
@Component
@Instantiate
public class ConsoleFactory {

    /**
     * Http Service
     */
    @Requires
    HttpService httpService;

    /**
     * UI provider factory
     */
    @Requires
    IUIProviderFactory uiProviderFactory;

    /**
     * Resource base
     */
    final static String RESOURCE_BASE = "/VAADIN";

    /**
     * Start ipojo component
     */
    @Validate
    public void start() {
        //try {
            // Register resources
            //httpService.registerResources(RESOURCE_BASE, RESOURCE_BASE, null);
        //} catch (NamespaceException e) {
        //    e.printStackTrace();
        //}
    }

    /**
     * Stop ipojo component
     */
    @Invalidate
    public void stop() {
        // Unregister resources
        httpService.unregister(RESOURCE_BASE);
    }

    /**
     * Bind a console
     * @param console
     */
    @Bind(aggregate = true, optional = true)
    public void bindConsole(IConsole console) {
        // Create an UI provider for the console UI
        UIProvider uiProvider = uiProviderFactory.createUIProvider(console);

        // Create a servlet
        VaadinOSGiServlet servlet = new VaadinOSGiServlet(uiProvider);

        try {
            // Register the servlet with the console alias
            httpService.registerServlet(console.getConsoleAlias(), servlet, null, null);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (NamespaceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unbind a console
     * @param console
     */
    @Unbind(aggregate = true, optional = true)
    public void unbindConsole(IConsole console) {
        // Unregister its servlet
        httpService.unregister(console.getConsoleAlias());
    }
}
