package com.peergreen.webconsole.core.context;

import com.peergreen.webconsole.ISecurityManager;
import com.peergreen.webconsole.UIContext;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 28/05/13
 * Time: 15:56
 * To change this template use File | Settings | File Templates.
 */
public class BaseUIContext implements UIContext {

    private ISecurityManager securityManager;

    public BaseUIContext(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Override
    public ISecurityManager getSecurityManager() {
        return securityManager;
    }
}
