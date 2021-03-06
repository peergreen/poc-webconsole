package com.peergreen.webconsole.modules.bundles;

import com.peergreen.webconsole.Extension;
import com.peergreen.webconsole.ExtensionPoint;
import com.peergreen.webconsole.Inject;
import com.peergreen.webconsole.Ready;
import com.peergreen.webconsole.scopes.test.Tab;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 14/05/13
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
@Extension
@ExtensionPoint("com.peergreen.webconsole.scopes.test.TestScope.tab")
@Tab(name = "OSGi Bundles")
public class BundlesModule extends VerticalLayout {

    @Inject
    private BundleContext bundleContext;
    private Table table;

    @Ready
    public void createView() {
        setMargin(true);
        setSpacing(true);

        table = new Table();
        table.addContainerProperty("Bundle Symbolic Name", String.class, null);
        table.addContainerProperty("Version", String.class, null);
        table.addContainerProperty("State", String.class, null);
        table.addContainerProperty("Active", CheckBox.class, null);
        table.setWidth("100%");
        table.setPageLength(20);
        table.setSortContainerPropertyId("Bundle Symbolic Name");
        table.setSortAscending(true);
        table.setImmediate(true);

        refreshTable();
        addComponent(table);
    }

    private void refreshTable() {
        Bundle[] bundles = bundleContext.getBundles();
        table.removeAllItems();

        int i = 1;
        for (Bundle bundle : bundles) {
            final Bundle selectedBundle = bundle;
            CheckBox checkBox = new CheckBox();
            checkBox.setImmediate(true);
            checkBox.setValue(bundle.getState() == Bundle.ACTIVE);
            checkBox.addValueChangeListener(new ValueChangeListener() {

                @Override
                public void valueChange(
                        com.vaadin.data.Property.ValueChangeEvent event)
                {
                    if (selectedBundle.getState() == Bundle.ACTIVE) {
                        try {
                            selectedBundle.stop();
                            refreshTable();
                        } catch (BundleException e1) {
                            e1.printStackTrace();
                        }
                    } else if (selectedBundle.getState() == Bundle.RESOLVED) {
                        try {
                            selectedBundle.start();
                            refreshTable();
                        } catch (BundleException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            table.addItem(
                    new Object[] {
                            bundle.getSymbolicName(),
                            bundle.getVersion().toString(),
                            getStateString(bundle),
                            checkBox
                    },
                    i++);
        }
        table.sort();
    }

    String getStateString(Bundle bundle) {
        switch (bundle.getState()) {
            case Bundle.ACTIVE:
                return "ACTIVE";
            case Bundle.INSTALLED:
                return "INSTALLED";
            case Bundle.RESOLVED:
                return "RESOLVED";
            case Bundle.UNINSTALLED:
                return "UNINSTALLED";
            default:
                return "UNKNOWN";
        }
    }
}
