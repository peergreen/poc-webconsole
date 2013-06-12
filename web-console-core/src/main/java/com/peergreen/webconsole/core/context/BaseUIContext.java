package com.peergreen.webconsole.core.context;

import com.peergreen.webconsole.ISecurityManager;
import com.peergreen.webconsole.UIContext;

/**
 * @author Mohammed Boukada
 */
public class BaseUIContext implements UIContext {

    private ISecurityManager securityManager;
    private int uiId;

    public BaseUIContext(ISecurityManager securityManager, int uiId) {
        this.securityManager = securityManager;
        this.uiId = uiId;
    }

    @Override
    public ISecurityManager getSecurityManager() {
        return securityManager;
    }

    @Override
    public int getUIId() {
        return uiId;
    }
}
