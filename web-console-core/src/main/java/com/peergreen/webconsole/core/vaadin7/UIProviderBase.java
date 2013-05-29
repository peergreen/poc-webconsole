package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.IConsole;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;
import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.MissingHandlerException;
import org.apache.felix.ipojo.UnacceptableConfiguration;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.Properties;

/**
 * Vaadin Base console UI provider
 * @author Mohammed Boukada
 */
@Component
@Provides(specifications = UIProvider.class)
public class UIProviderBase extends UIProvider {

    /**
     * Console
     */
    private IConsole console;

    /**
     * Base console UI ipojo component factory
     */
    @Requires(from = "com.peergreen.webconsole.core.vaadin7.BaseUI")
    private Factory factory;

    /**
     * Bundle context
     */
    private BundleContext bundleContext;

    /**
     * Vaadin base UI provider constructor
     * @param bundleContext
     */
    public UIProviderBase(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /**
     * Set console
     * @param console
     */
    public void setConsole(IConsole console) {
        this.console = console;
    }

    /** {@inheritDoc}
     */
    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return BaseUI.class;
    }

    /** {@inheritDoc}
     */
    @Override
    public UI createInstance(final UICreateEvent e) {

        UI ui = null;
        try {
            // Create an instance of baseUI
            BaseUI newUi = new BaseUI(console.getConsoleName());

            // Configuration properties for ipojo component
            Properties props = new Properties();
            // Use the instance of baseUI an pojo instance for ipojo component
            props.put("instance.object", newUi);

            // Create ipojo component from its factory
            ComponentInstance instance = factory.createComponentInstance(props);

            // Retrieve service from service reference
            ServiceReference[] refs = bundleContext.getServiceReferences(BaseUI.class.getName(),
                            "(instance.name=" + instance.getInstanceName() +")");
            if (refs != null) {
                // Gets the created UI as an ipojo component
                ui = (UI) bundleContext.getService(refs[0]);
            }
        } catch (UnacceptableConfiguration unacceptableConfiguration) {
            unacceptableConfiguration.printStackTrace();
        } catch (MissingHandlerException ex) {
            ex.printStackTrace();
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
        } catch (InvalidSyntaxException ex) {
            ex.printStackTrace();
        }

        return ui;
    }
}
