package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.core.api.IConsole;
import com.peergreen.webconsole.core.api.IUIProviderFactory;
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
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 22/05/13
 * Time: 10:11
 * To change this template use File | Settings | File Templates.
 */
@Component
@Instantiate
@Provides
public class UIProviderFactory implements IUIProviderFactory {

    @Requires(from = "com.peergreen.webconsole.core.vaadin7.UIProviderBase")
    private Factory factory;

    private BundleContext bundleContext;

    public UIProviderFactory(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public UIProvider createUIProvider(IConsole console) {
        UIProvider provider = null;
        try {
            UIProviderBase newUIProvider = new UIProviderBase(bundleContext);
            newUIProvider.setConsole(console);
            Properties props = new Properties();
            props.put("instance.object", newUIProvider);
            ComponentInstance instance = factory.createComponentInstance(props);
            ServiceReference[] refs = bundleContext.getServiceReferences(UIProvider.class.getName(),
                            "(instance.name=" + instance.getInstanceName() +")");
            if (refs != null) {
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
