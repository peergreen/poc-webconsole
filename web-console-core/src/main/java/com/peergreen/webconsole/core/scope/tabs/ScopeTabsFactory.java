package com.peergreen.webconsole.core.scope.tabs;

import com.peergreen.webconsole.scope.IScopeFactory;
import com.peergreen.webconsole.scope.IScopeTabsFactory;
import com.peergreen.webconsole.IViewIPojoInstanceGarbageCollector;
import com.peergreen.webconsole.UIContext;
import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.MissingHandlerException;
import org.apache.felix.ipojo.UnacceptableConfiguration;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Tabs scope factory
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides
public class ScopeTabsFactory implements IScopeTabsFactory {

    /**
     * Scope tabs view ipojo component factory
     */
    @Requires(from = "com.peergreen.webconsole.core.scope.tabs.ScopeTabsView")
    private Factory factory;

    /**
     * IPojo instance of views garbage collector
     */
    @Requires
    private IViewIPojoInstanceGarbageCollector viewIPojoInstanceGarbageCollector;

    /**
     * Bundle context
     */
    private BundleContext bundleContext;

    /**
     * Ipojo component instances of each scope tabs view
     */
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<ComponentInstance>> instances = new ConcurrentHashMap<>();

    /**
     * Scope tabs view constructor
     * @param bundleContext
     */
    public ScopeTabsFactory(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /** {@inheritDoc}
     */
    @Override
    public com.vaadin.ui.Component createInstance(String scopeName, UIContext context) {
        return createInstance(scopeName, new ArrayList<String>(), false, context);
    }

    /** {@inheritDoc}
     */
    @Override
    public com.vaadin.ui.Component createInstance(String scopeName, List<String> scopesRange, UIContext context) {
        return createInstance(scopeName, scopesRange, false, context);
    }

    /** {@inheritDoc}
     */
    @Override
    public com.vaadin.ui.Component createInstance(String scopeName, boolean isDefaultScope, UIContext context) {
        return createInstance(scopeName, new ArrayList<String>(), isDefaultScope, context);
    }

    /** {@inheritDoc}
     */
    @Override
    public com.vaadin.ui.Component createInstance(String scopeName, List<String> scopesRange, boolean isDefaultScope, UIContext context) {
        ScopeTabsView scope = null;
        try {
            ScopeTabsView scopeTabsView = new ScopeTabsView(scopeName, scopesRange, isDefaultScope, context);
            Properties props = new Properties();
            props.put("instance.object", scopeTabsView);
            ComponentInstance instance = factory.createComponentInstance(props);
            addInstance(scopeName, instance);
            ServiceReference[] refs = bundleContext.getServiceReferences(ScopeTabsView.class.getName(),
                    "(instance.name=" + instance.getInstanceName() +")");
            if (refs != null) {
                scope = (ScopeTabsView) bundleContext.getService(refs[0]);
            }
            viewIPojoInstanceGarbageCollector.addView(scope, instance);
        } catch (UnacceptableConfiguration unacceptableConfiguration) {
            unacceptableConfiguration.printStackTrace();
        } catch (MissingHandlerException ex) {
            ex.printStackTrace();
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
        } catch (InvalidSyntaxException ex) {
            ex.printStackTrace();
        }

        return scope;
    }

    /**
     * Stop scope ipojo component instances
     * @param scopeName
     */
    private void stopScopeInstances(String scopeName) {
        for (ComponentInstance instance : instances.get(scopeName)) {
            instance.dispose();
        }
        instances.remove(scopeName);
    }

    //TODO useless but required for ipojo
    @Bind(aggregate = true, optional = true)
    public void bindScope(IScopeFactory scopeFactory) {

    }

    /**
     * Unbind a scope factory
     * @param scopeFactory
     */
    @Unbind
    public void unbindScope(IScopeFactory scopeFactory) {
        try {
            if (instances.containsKey(scopeFactory.getSymbolicName())) {
                stopScopeInstances(scopeFactory.getSymbolicName());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Add ipojo component instance for a scope
     * @param scopeName
     * @param instance
     */
    private void addInstance(String scopeName, ComponentInstance instance) {
        ConcurrentLinkedQueue<ComponentInstance> componentInstances;
        if (instances.containsKey(scopeName)) {
            componentInstances = instances.get(scopeName);
            componentInstances.add(instance);
        } else {
            componentInstances = new ConcurrentLinkedQueue<>();
            componentInstances.add(instance);
            instances.put(scopeName, componentInstances);
        }
    }
}