package com.peergreen.webconsole.core.osgi;

import com.peergreen.newsfeed.RssService;
import com.peergreen.webconsole.core.api.IVaadinUI;
import com.peergreen.webconsole.core.api.IViewContribution;
import com.peergreen.webconsole.core.vaadin7.MainUI;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;

import java.util.ArrayList;
import java.util.List;

@Component
@Instantiate
@Provides(specifications = UIProvider.class)
public class OSGiUIProvider extends UIProvider {

    private static final long serialVersionUID = 1451931523729856181L;

    private List<IViewContribution> views = new ArrayList<>();

    private List<UI> uis = new ArrayList<>();

    @Requires
    RssService rssService;

    @Override
    public Class<? extends UI> getUIClass(final UIClassSelectionEvent event) {
        return MainUI.class;
    }

    @Override
    public UI createInstance(final UICreateEvent e) {
        UI ui = new MainUI(views, rssService);
        uis.add(ui);
        return ui;
    }

    @Bind(aggregate = true, optional = true)
    public void bindViewContribution(IViewContribution viewContribution) {
        views.add(viewContribution);
        for (UI ui : uis) {
            if (ui != null) {
                ((IVaadinUI) ui).addView(viewContribution);
            } else {
                uis.remove(ui);
            }
        }
    }

    @Unbind(aggregate = true, optional = true)
    public void unbindViewContribution(IViewContribution viewContribution) {
        views.remove(viewContribution);
        for (UI ui : uis) {
            if (ui != null) {
                ((IVaadinUI) ui).removeView(viewContribution);
            } else {
                uis.remove(ui);
            }
        }
    }

}
