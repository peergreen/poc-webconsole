package com.peergreen.webconsole.core.api;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 02/05/13
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public interface IViewContribution {

    public Component getView(UI application);

    public String getIcon();

    public String getName();
}
