package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.Constants;
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

import java.util.Dictionary;
import java.util.Hashtable;

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

    private int uiId = 0;

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

        BaseUI ui = null;
        try {
            // Create an instance of baseUI
            String scopeExtensionPoint = "com.peergreen.webconsole." + console.getConsoleAlias().substring(1) + ".scope";
            ui = new BaseUI(console.getConsoleName(), scopeExtensionPoint, uiId);

            // Configuration properties for ipojo component
            Dictionary<String, Object> props = new Hashtable<>();
            props.put("instance.object", ui);
            Dictionary<String, Object> bindFilters = new Hashtable<>();
            bindFilters.put("ScopeView", "(&(" + Constants.UI_ID + "=" + uiId + ")(" +
                    Constants.EXTENSION_POINT + "=" + scopeExtensionPoint + "))");
            props.put(Constants.REQUIRES_FILTER, bindFilters);

            // Create ipojo component from its factory
            ComponentInstance instance = factory.createComponentInstance(props);
            uiId++;
        } catch (UnacceptableConfiguration unacceptableConfiguration) {
            unacceptableConfiguration.printStackTrace();
        } catch (MissingHandlerException ex) {
            ex.printStackTrace();
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
        }

        return ui;
    }
}
