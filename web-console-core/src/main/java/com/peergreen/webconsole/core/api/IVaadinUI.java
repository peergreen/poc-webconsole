package com.peergreen.webconsole.core.api;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 14/05/13
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public interface IVaadinUI extends IUiManager {

    void addScope(IScopeProvider scope);

    void removeScope(IScopeProvider scope);

}
