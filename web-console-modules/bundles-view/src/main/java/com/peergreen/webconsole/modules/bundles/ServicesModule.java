package com.peergreen.webconsole.modules.bundles;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.peergreen.webconsole.core.api.IModuleFactory;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 14/05/13
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
@org.apache.felix.ipojo.annotations.Component
@Provides
@Instantiate
public class ServicesModule implements IModuleFactory {

    private final BundleContext bundleContext;
    private Table table;

    public ServicesModule(final BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public Component getView() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);

        table = new Table();

        table.addContainerProperty("service-id", Long.class, null);
        table.addContainerProperty("interfaces", String.class, null);
        table.addContainerProperty("bundle", String.class, null);

        table.setColumnHeader("service-id", "Service ID");
        table.setColumnHeader("interfaces", "Interfaces");
        table.setColumnHeader("bundle", "Bundle");

        table.setWidth("100%");
        table.setPageLength(20);
        table.setSortContainerPropertyId("service-id");
        table.setSortAscending(true);
        table.setImmediate(true);

        refreshTable();

        verticalLayout.addComponent(table);
        return verticalLayout;
    }

    private void refreshTable() {
        Bundle[] bundles = bundleContext.getBundles();
        table.removeAllItems();

        for (Bundle bundle : bundles) {
            if (bundle.getRegisteredServices() != null) {
                for (ServiceReference<?> reference : bundle.getRegisteredServices()) {
                    table.addItem(
                            new Object[] {
                                    reference.getProperty(Constants.SERVICE_ID),
                                    getInterfaces(reference),
                                    format("%s (%d)", bundle.getSymbolicName(), bundle.getBundleId())
                            }, null);
                }
            }
        }
        table.sort();
    }

    String getInterfaces(ServiceReference<?> reference) {
        List<String> interfaces = new ArrayList<>();
        String[] classes = (String[]) reference.getProperty(Constants.OBJECTCLASS);
        interfaces.addAll(Arrays.asList(classes));
        return interfaces.toString();
    }

    @Override
    public String getScope() {
        return "osgi";
    }

    @Override
    public String getName() {
        return "Services";
    }

}
