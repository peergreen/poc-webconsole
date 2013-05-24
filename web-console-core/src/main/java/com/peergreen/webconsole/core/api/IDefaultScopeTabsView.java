package com.peergreen.webconsole.core.api;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 24/05/13
 * Time: 09:35
 * To change this template use File | Settings | File Templates.
 */
public interface IDefaultScopeTabsView {

    String getScopeName();

    void addScopelessModules(ConcurrentLinkedQueue<IModuleFactory> modules);

}
