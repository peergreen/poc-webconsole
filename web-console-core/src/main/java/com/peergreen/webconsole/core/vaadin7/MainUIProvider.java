package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.core.api.IHelpManager;
import com.peergreen.webconsole.core.api.IScopeProvider;
import com.peergreen.webconsole.core.api.IUiManager;
import com.peergreen.webconsole.core.api.IVaadinUI;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the main view of web console
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides(specifications = {UIProvider.class, IUiManager.class})
public class MainUIProvider extends UIProvider implements IUiManager {

    private static final long serialVersionUID = 1451931523729856181L;

    /**
     * List of scopes in the main view
     */
    private List<IScopeProvider> scopes = new ArrayList<>();

    /**
     * First instance of UI
     */
    private UI ui;

    /**
     * Instances of Uis
     */
    private List<UI> uis = new ArrayList<>();

    /**
     * To define the first instantiation of an UI
     */
    private boolean firstCall;

    /**
     * Main scope
     */
    private String mainScope = "";

    /**
     * Help Manager
     */
    @Requires
    private IHelpManager helpManager;

    /**
     * Get main UI class
     * @param event
     * @return main UI class
     */
    @Override
    public Class<? extends UI> getUIClass(final UIClassSelectionEvent event) {
        return MainUI.class;
    }

    /**
     * Create an instance of main UI
     * @param e
     * @return instance of main UI
     */
    @Override
    public UI createInstance(final UICreateEvent e) {
        if (firstCall) {
            firstCall = false;
            return ui;
        }
        UI newUi = new MainUI(scopes, helpManager, mainScope);
        uis.add(newUi);
        return newUi;
    }

    /**
     * Register a new scope
     * @param scope
     */
    @Bind(aggregate = true, optional = true)
    public void bindScopeContribution(IScopeProvider scope) {
        scopes.add(scope);
        updateUIs(scope, false);
    }

    /**
     * Unregister a scope
     * @param scope
     */
    @Unbind(aggregate = true, optional = true)
    public void unbindScopeContribution(IScopeProvider scope) {
        scopes.remove(scope);
        updateUIs(scope, true);
    }

    /**
     * Update instances of main UIs when changes affect scopes
     * @param scope
     * @param remove whether the scope was unbind
     */
    private void updateUIs(IScopeProvider scope, boolean remove) {
        List<UI> uiToRemove = new ArrayList<>();
        for (UI ui : uis) {
            if (ui != null) {
                if (remove) {
                    ((IVaadinUI) ui).removeScope(scope);
                } else {
                    ((IVaadinUI) ui).addScope(scope);
                }
            } else {
                uiToRemove.add(ui);
            }
        }

        for (UI ui : uiToRemove) {
            uis.remove(ui);
        }
    }

    /**
     * Update scope badge
     * @param scopeName
     */
    @Override
    public void updateScopeBadge(String scopeName) {
        List<UI> uiToRemove = new ArrayList<>();
        for (UI ui : uis) {
            if (ui != null) {
                ((IVaadinUI) ui).updateScopeBadge(scopeName);
            } else {
                uiToRemove.add(ui);
            }
        }

        for (UI ui : uiToRemove) {
            uis.remove(ui);
        }
    }

    /**
     * Update scope menu button
     * @param scopeName
     */
    @Override
    public void updateScopeMenuButton(String scopeName) {
        List<UI> uiToRemove = new ArrayList<>();
        for (UI ui : uis) {
            if (ui != null) {
                ((IVaadinUI) ui).updateScopeMenuButton(scopeName);
            } else {
                uiToRemove.add(ui);
            }
        }

        for (UI ui : uiToRemove) {
            uis.remove(ui);
        }
    }

    /**
     * Set main scope
     * @param scopeName
     */
    @Override
    public void setMainScope(String scopeName) {
        mainScope = scopeName;
        List<UI> uiToRemove = new ArrayList<>();
        for (UI ui : uis) {
            if (ui != null) {
                ((IVaadinUI) ui).setMainScope(scopeName);
            } else {
                uiToRemove.add(ui);
            }
        }

        for (UI ui : uiToRemove) {
            uis.remove(ui);
        }
    }

    @Validate
    public void start() {
        ui = new MainUI(scopes, helpManager, mainScope);
        uis.add(ui);
        firstCall = true;
    }

    @Invalidate
    public void stop() {

    }
}
