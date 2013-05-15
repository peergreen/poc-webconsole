package com.peergreen.webconsole.modules.bundles;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 15/05/13
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
public class Activator implements BundleActivator {

    private static Bundle[] bundles;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        bundles = bundleContext.getBundles();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }

    public static synchronized Bundle[] getBundles() {
        return bundles;
    }
}
