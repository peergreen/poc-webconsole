package com.peergreen.webconsole.core.scope;

import com.peergreen.webconsole.core.api.INotifierService;
import com.peergreen.webconsole.core.api.IScopeFactory;
import com.peergreen.webconsole.core.api.IScopeTabsFactory;
import com.vaadin.navigator.View;
import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.MissingHandlerException;
import org.apache.felix.ipojo.UnacceptableConfiguration;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
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
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 22/05/13
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
@Component
@Instantiate
@Provides
public class ScopeTabsFactory implements IScopeTabsFactory {

    @Requires
    INotifierService notifierService;

    @Requires(from = "com.peergreen.webconsole.core.scope.ScopeTabsView")
    private Factory factory;

    private BundleContext bundleContext;

    private ConcurrentHashMap<String, ConcurrentLinkedQueue<ComponentInstance>> instances = new ConcurrentHashMap<>();

    public ScopeTabsFactory(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public View createInstance(String scopeName) {
        return createInstance(scopeName, false);
    }

    @Override
    public View createInstance(String scopeName, boolean isDefaultScope) {
        ScopeTabsView scope = null;
        try {
            ScopeTabsView scopeTabsView = new ScopeTabsView(scopeName, isDefaultScope);
            Properties props = new Properties();
            props.put("instance.object", scopeTabsView);
            ComponentInstance instance = factory.createComponentInstance(props);
            addInstance(scopeName, instance);
            ServiceReference[] refs = bundleContext.getServiceReferences(ScopeTabsView.class.getName(),
                    "(instance.name=" + instance.getInstanceName() +")");
            if (refs != null) {
                scope = (ScopeTabsView) bundleContext.getService(refs[0]);
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

        return scope;
    }

    private void stopScopeInstances(String scopeName) {
        for (ComponentInstance instance : instances.get(scopeName)) {
            instance.dispose();
        }
        instances.remove(scopeName);
    }

    @Bind(aggregate = true, optional = true)
    public void bindScope(IScopeFactory scopeFactory) {

    }

    @Unbind
    public void unbindScope(IScopeFactory scopeFactory) {
        try {
            if (instances.containsKey(scopeFactory.getName())) {
                stopScopeInstances(scopeFactory.getName());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

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
