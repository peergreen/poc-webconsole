package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.core.api.IConsole;
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
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 22/05/13
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */
@Component
@Provides(specifications = UIProvider.class)
public class UIProviderBase extends UIProvider {

    private IConsole console;

    @Requires(from = "com.peergreen.webconsole.core.vaadin7.BaseUI")
    private Factory factory;

    private BundleContext bundleContext;

    public UIProviderBase(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public void setConsole(IConsole console) {
        this.console = console;
    }

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return BaseUI.class;
    }

    @Override
    public UI createInstance(final UICreateEvent e) {

        UI ui = null;
        try {
            BaseUI newUi = new BaseUI(console.getConsoleName());
            Properties props = new Properties();
            props.put("instance.object", newUi);
            ComponentInstance instance = factory.createComponentInstance(props);
            ServiceReference[] refs = bundleContext.getServiceReferences(BaseUI.class.getName(),
                            "(instance.name=" + instance.getInstanceName() +")");
            if (refs != null) {
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
