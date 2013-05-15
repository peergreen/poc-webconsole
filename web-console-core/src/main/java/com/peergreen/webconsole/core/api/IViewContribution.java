package com.peergreen.webconsole.core.api;

import com.vaadin.ui.Component;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 02/05/13
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public interface IViewContribution {

    public Component getView();

    public String getScope();

    public String getName();
}
