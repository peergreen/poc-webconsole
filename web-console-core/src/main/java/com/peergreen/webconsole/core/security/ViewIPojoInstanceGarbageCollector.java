package com.peergreen.webconsole.core.security;

import com.peergreen.webconsole.core.api.IViewIPojoInstanceGarbageCollector;
import com.vaadin.ui.Component;
import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 28/05/13
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
@org.apache.felix.ipojo.annotations.Component
@Instantiate
@Provides
public class ViewIPojoInstanceGarbageCollector implements IViewIPojoInstanceGarbageCollector {

    private ConcurrentHashMap<Component, ComponentInstance> instances = new ConcurrentHashMap<>();

    @Override
    public void addView(Component component, ComponentInstance componentInstance) {
        instances.put(component, componentInstance);
    }

    @Override
    public void removeView(Component component) {
        if (instances.containsKey(component)) {
            instances.get(component).dispose();
            instances.remove(component);
        }
    }
}
