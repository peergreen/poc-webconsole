package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.IConsole;
import com.peergreen.webconsole.core.IUIProviderFactory;
import com.vaadin.server.UIProvider;
import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.MissingHandlerException;
import org.apache.felix.ipojo.UnacceptableConfiguration;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.Properties;

/**
 * Vaadin UI provider factory
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides
public class UIProviderFactory implements IUIProviderFactory {

    /**
     * Base console UI provider ipojo component factory
     */
    @Requires(from = "com.peergreen.webconsole.core.vaadin7.UIProviderBase")
    private Factory factory;

    /**
     * Bundle context
     */
    private BundleContext bundleContext;

    /**
     * Vaadin UI provider factory constructor
     * @param bundleContext
     */
    public UIProviderFactory(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /** {@inheritDoc}
     */
    @Override
    public UIProvider createUIProvider(IConsole console) {
        UIProvider provider = null;
        try {
            // Create an instance of base console UI provider
            UIProviderBase newUIProvider = new UIProviderBase(bundleContext);
            newUIProvider.setConsole(console);

            // Configuration properties for ipojo component
            Properties props = new Properties();
            // Use the instance of baseUI an pojo instance for ipojo component
            props.put("instance.object", newUIProvider);

            // Create ipojo component from its factory
            ComponentInstance instance = factory.createComponentInstance(props);

            // Retrieve service from service reference
            ServiceReference[] refs = bundleContext.getServiceReferences(UIProvider.class.getName(),
                            "(instance.name=" + instance.getInstanceName() +")");
            if (refs != null) {
                // Gets the created UI provider as an ipojo component
                provider = (UIProvider) bundleContext.getService(refs[0]);
            }
        } catch (UnacceptableConfiguration unacceptableConfiguration) {
            unacceptableConfiguration.printStackTrace();
        } catch (MissingHandlerException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
        return provider;
    }
}
