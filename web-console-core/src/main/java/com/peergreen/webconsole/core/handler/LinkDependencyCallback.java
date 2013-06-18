package com.peergreen.webconsole.core.handler;

import com.vaadin.ui.UI;
import org.apache.felix.ipojo.InstanceManager;
import org.apache.felix.ipojo.handlers.dependency.Dependency;
import org.apache.felix.ipojo.handlers.dependency.DependencyCallback;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mohammed Boukada
 */
public class LinkDependencyCallback extends DependencyCallback {

    private UI ui;
    private InstanceManager manager;

    /**
     * Constructor.
     *
     * @param dep        : the dependency attached to this dependency callback
     * @param method     : the method to call
     * @param methodType : is the method to call a bind method or an unbind
     *                   method
     */
    public LinkDependencyCallback(Dependency dep, String method, int methodType, UI ui) {
        super(dep, method, methodType);
        this.ui = ui;
        this.manager = dep.getHandler().getInstanceManager();
    }

    @Override
    public Object call(final Object[] arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (m_methodObj == null) {
            searchMethod();
        }
        final Object[] newObject = {null};
        // Two cases :
        // - if instances already exists : call on each instances
        // - if no instance exists : create an instance
        if (manager.getPojoObjects() == null) {
            ui.access(new Runnable() {
                @Override
                public void run() {
                    try {
                        newObject[0] =  m_methodObj.invoke(manager.getPojoObject(), arg);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            for (int i = 0; i < manager.getPojoObjects().length; i++) {
                final int finalI = i;
                ui.access(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            newObject[0] = m_methodObj.invoke(manager.getPojoObjects()[finalI], arg);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        return newObject[0];
    }

    @Override
    public Object call(final Object instance, final Object[] arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (m_methodObj == null) {
            searchMethod();
        }
        final Object[] newObject = {null};
        ui.access(new Runnable() {
            @Override
            public void run() {
                try {
                    newObject[0] = m_methodObj.invoke(instance, arg);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        return newObject[0];
    }
}
