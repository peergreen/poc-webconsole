package com.peergreen.webconsole.core;

import com.peergreen.webconsole.core.api.IViewContribution;
import com.peergreen.webconsole.core.osgi.VaadinOSGiServlet;

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
 * Date: 02/05/13
 * Time: 15:47
 * To change this template use File | Settings | File Templates.
 */

@Component(name="WebConsoleComponent")
@Instantiate
public class MainApplication {

    @Requires
    private HttpService httpService;

    private static final String ALIAS = "/pgadmin";

    private static final String RESOURCE_BASE = "/VAADIN";

    @Validate
    public void start() throws ServletException, NamespaceException {
        httpService.registerServlet(ALIAS, new VaadinOSGiServlet(), null, null);
        httpService.registerServlet(ALIAS + "/*", new VaadinOSGiServlet(), null, null);
        httpService.registerResources(RESOURCE_BASE, RESOURCE_BASE, null);
    }

    @Invalidate
    public void stop() {
        httpService.unregister(ALIAS);
        httpService.unregister(ALIAS + "/*");
        httpService.unregister(RESOURCE_BASE);
    }

    @Bind(aggregate = true, optional = true)
    public void bindViewContribution(IViewContribution viewContribution) {
    }

    @Unbind(aggregate = true, optional = true)
    public void unbindViewContribution(IViewContribution viewContribution) {
    }
}
