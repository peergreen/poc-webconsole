package com.peergreen.webconsole.core;

import com.peergreen.webconsole.core.api.IConsole;
import com.peergreen.webconsole.core.api.IUIProviderFactory;
import com.peergreen.webconsole.core.osgi.VaadinOSGiServlet;
import com.vaadin.server.UIProvider;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 22/05/13
 * Time: 10:09
 * To change this template use File | Settings | File Templates.
 */
@Component
@Instantiate
public class ConsoleFactory {

    @Requires
    HttpService httpService;

    @Requires
    IUIProviderFactory uiProviderFactory;

    final static String RESOURCE_BASE = "/VAADIN";

    @Validate
    public void start() {
        try {
            httpService.registerResources(RESOURCE_BASE, RESOURCE_BASE, null);
        } catch (NamespaceException e) {
            e.printStackTrace();
        }
    }

    @Invalidate
    public void stop() {
        httpService.unregister(RESOURCE_BASE);
    }

    @Bind(aggregate = true, optional = true)
    public void bindConsole(IConsole console) {
        UIProvider uiProvider = uiProviderFactory.createUIProvider(console);
        VaadinOSGiServlet servlet = new VaadinOSGiServlet(uiProvider);

        try {
            httpService.registerServlet(console.getConsoleAlias(), servlet, null, null);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (NamespaceException e) {
            e.printStackTrace();
        }
    }

    @Unbind(aggregate = true, optional = true)
    public void unbindConsole(IConsole console) {
        httpService.unregister(console.getConsoleAlias());
    }
}
