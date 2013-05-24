package com.peergreen.webconsole.core.osgi;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

public class VaadinOSGiServlet extends VaadinServlet {

    UIProvider provider;

    public VaadinOSGiServlet(UIProvider provider) {
        this.provider = provider;
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
