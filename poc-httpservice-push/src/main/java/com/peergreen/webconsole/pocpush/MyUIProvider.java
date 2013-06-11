package com.peergreen.webconsole.pocpush;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

/**
 * Vaadin Base console UI provider
 * @author Mohammed Boukada
 */
@Component
@Provides(specifications = UIProvider.class)
@Instantiate
public class MyUIProvider extends UIProvider {


    private final List<MyUI> listUI;
    private final List<MyComponentFactory> componentFactories;

    /**
     * Vaadin base UI provider constructor
     */
    public MyUIProvider() {
        this.listUI =  new CopyOnWriteArrayList<>();
        this.componentFactories =  new CopyOnWriteArrayList<>();
    }

    @Bind(aggregate=true, optional=true)
    public void bindComponent(final MyComponentFactory componentFactory) {
        componentFactories.add(componentFactory);
        for (int i=0; i < listUI.size(); i++) {
            final MyUI ui = listUI.get(i);
            ui.access(new Runnable() {

                @Override
                public void run() {
                    ui.getLayout().addComponent(componentFactory.create());

                }
            });
        }
    }

    @Unbind(aggregate=true, optional=true)
    public void unbindComponent(final MyComponentFactory componentFactory) {
        componentFactories.remove(componentFactory);
        for (int i=0; i < listUI.size(); i++) {
            final MyUI ui = listUI.get(i);
            ui.access(new Runnable() {

                @Override
                public void run() {
                    //FIXME : pure hack by removing all previous elements
                    // DO NOT USE this in production
                    ui.getLayout().removeAllComponents();
                    for (MyComponentFactory componentFactory : componentFactories) {
                        ui.getLayout().addComponent(componentFactory.create());
                    }
                }
            });

        }
    }


    /** {@inheritDoc}
     */
    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return MyUI.class;
    }

    /** {@inheritDoc}
     */
    @Override
    public UI createInstance(final UICreateEvent e) {
        final MyUI ui = new MyUI();
        listUI.add(ui);
        for (MyComponentFactory componentFactory : componentFactories) {
            ui.getLayout().addComponent(componentFactory.create());
        }
        return ui;
    }
}
