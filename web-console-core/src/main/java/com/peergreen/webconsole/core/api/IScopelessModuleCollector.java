package com.peergreen.webconsole.core.api;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 23/05/13
 * Time: 11:04
 * To change this template use File | Settings | File Templates.
 */
public interface IScopelessModuleCollector {

    void addModule(IModuleFactory moduleFactory);

    void removeModule(IModuleFactory moduleFactory);

    void notifyDefaultScopes();
}
