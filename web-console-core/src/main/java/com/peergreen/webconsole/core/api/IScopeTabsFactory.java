package com.peergreen.webconsole.core.api;

import com.vaadin.navigator.View;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 22/05/13
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public interface IScopeTabsFactory {

    View createInstance(String scopeName);

    View createInstance(String scopeName, boolean isDefaultScope);

}
