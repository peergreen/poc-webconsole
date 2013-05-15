package com.peergreen.webconsole.core.api;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 14/05/13
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public interface IVaadinUI {

    void addView(IViewContribution view);

    void removeView(IViewContribution view);
}
