package com.peergreen.webconsole.core.api;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 17/05/13
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
public interface IScopeView {

    void addView(IViewContribution view);

    void removeView(IViewContribution view);

}
