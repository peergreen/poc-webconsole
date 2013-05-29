package com.peergreen.webconsole;

import com.vaadin.ui.Component;
import org.apache.felix.ipojo.ComponentInstance;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 28/05/13
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public interface IViewIPojoInstanceGarbageCollector {

    void addView(Component component, ComponentInstance componentInstance);

    void removeView(Component component);

}
