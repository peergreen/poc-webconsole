package com.peergreen.webconsole.core.osgi;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;

@Component
@Instantiate
public class VaadinOSGiServlet extends VaadinServlet {

    @Requires
    com.vaadin.server.UIProvider provider;

    @Requires
    private HttpService httpService;

    private static final String ALIAS = "/pgadmin";

    private static final String RESOURCE_BASE = "/VAADIN";

    @Validate
    public void start() throws ServletException, NamespaceException {
        httpService.registerServlet(ALIAS, this, null, null);
        httpService.registerResources(RESOURCE_BASE, RESOURCE_BASE, null);
    }

    @Invalidate
    public void stop() {
        httpService.unregister(ALIAS);
        httpService.unregister(RESOURCE_BASE);
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {

        final VaadinServletService service = super.createServletService(deploymentConfiguration);

        service.addSessionInitListener(new SessionInitListener() {
            private static final long serialVersionUID = -3430847247361456116L;

            @Override
            public void sessionInit(SessionInitEvent e) throws ServiceException {
                e.getSession().addUIProvider(provider);
            }
         });

        return service;
    }
}
