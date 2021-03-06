package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.security.UsernamePasswordAuthenticateService;
import com.peergreen.webconsole.Constants;
import com.peergreen.webconsole.ExtensionFactory;
import com.peergreen.webconsole.INotifierService;
import com.peergreen.webconsole.core.scope.NavigatorView;
import com.peergreen.webconsole.core.scope.Scope;
import com.peergreen.webconsole.ISecurityManager;
import com.peergreen.webconsole.core.context.BaseUIContext;
import com.peergreen.webconsole.core.exception.ExceptionView;
import com.peergreen.webconsole.core.scope.ScopeFactory;
import com.peergreen.webconsole.core.security.SecurityManager;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.util.CurrentInstance;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;

import javax.security.auth.Subject;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base console UI
 * @author Mohammed Boukada
 */
@Theme("dashboard")
@PreserveOnRefresh
@org.apache.felix.ipojo.annotations.Component
@Push
public class BaseUI extends UI {

    /**
     * Root layout
     */
    CssLayout root = new CssLayout();

    /**
     * Login layout
     */
    VerticalLayout loginLayout;

    /**
     * Progress indicatorlayout
     */
    VerticalLayout progressIndicatorLayout;

    /**
     * Menu layout
     */
    CssLayout menu;

    /**
     * Content layout
     */
    CssLayout content;

    /**
     * Main content layout
     */
    HorizontalLayout main;

    /**
     * Progress indicator
     */
    ProgressIndicator progressIndicator = new ProgressIndicator(new Float(0.0));

    int nbScopesToBound = 0;

    /**
     * To navigate between scopesFactories views
     */
    private Navigator nav;

    /**
     * Scopes bound
     */
    private Map<ExtensionFactory, ScopeFactory> scopesFactories = new ConcurrentHashMap<>();
    private Map<String, Scope> scopes = new ConcurrentHashMap<>();

    /**
     * BaseConsole name
      */
    private String consoleName;
    private Boolean enableSecurity;

    private String scopeExtensionPoint;

    /**
     * Security manager
     */
    private ISecurityManager securityManager;

    /**
     * UI id
     */
    private String uiId;

    /**
     * Notifier service
     */
    @Requires
    private INotifierService notifierService;

    /**
     * Authentication service
     */
    @Requires
    private UsernamePasswordAuthenticateService authenticateService;

    /**
     * Base console UI constructor
     */
    public BaseUI(String consoleName, String extensionPoint, String uiId, Boolean enableSecurity) {
        this.consoleName = consoleName;
        this.scopeExtensionPoint = extensionPoint;
        this.uiId = uiId;
        this.enableSecurity = enableSecurity;
    }

    @Invalidate
    public void stop() {
        // Unregister Vaadin connector
        getConnectorTracker().unregisterConnector(getConnectorTracker().getConnector(getConnectorId()));
    }

    /**
     * Bind a scope factory
     * @param extensionFactory
     */
    @Bind(aggregate = true, optional = true)
    public void bindExtensionFactory(ExtensionFactory extensionFactory, Dictionary props) {
        if (canAddExtensionFactory(props)) {
            String roles[] = (String[]) props.get(Constants.EXTENSION_ROLES);
            scopesFactories.remove(extensionFactory);
            ScopeFactory scopeFactory = new ScopeFactory(roles);
            if (progressIndicator.getValue() >= 1) {
                if (isAllowedToShowScope(roles)) {
                    scopeFactory.setInstance(extensionFactory.create(new BaseUIContext(this, securityManager, uiId)));
                }
            }
            scopesFactories.put(extensionFactory, scopeFactory);
        }
    }

    /**
     * Unbind a scope factory
     * @param extensionFactory
     */
    @Unbind
    public void unbindExtensionFactory(ExtensionFactory extensionFactory) {
        if (scopesFactories.containsKey(extensionFactory)) {
            if (scopesFactories.get(extensionFactory).getInstance() != null) {
                scopesFactories.get(extensionFactory).getInstance().stop();
            }
            scopesFactories.remove(extensionFactory);
        }
    }

    private boolean canAddExtensionFactory(Dictionary props) {
        String extensionId = (String) props.get(Constants.EXTENSION_POINT);
        return extensionId != null && scopeExtensionPoint.equals(extensionId);
    }

    @Bind(aggregate = true, optional = true)
    public void bindScopeView(Component scopeView, Dictionary props) {
        String scopeName = (String) props.get("scope.value");
        Scope scope = new Scope(scopeName, scopeView);
        scopes.put(scopeName, scope);
        addRouteToNav(scopeName, scopeView);
        addScopeButtonInMenu(scopeName, scopeView, progressIndicator.getValue() >= 1);
    }

    @Unbind
    public void unbindScopeView(Component scopeView, Dictionary props) {
        String scopeName = (String) props.get("scope.value");
        removeRouteFromNav(scopeName);
        removeScopeButtonInMenu(scopeName);
        scopes.remove(scopeName);
    }

    /**
     * Init UI
     * @param request
     */
    @Override
    protected void init(VaadinRequest request) {
        setLocale(Locale.US);
        getPage().setTitle("Welcome to " + consoleName);
        setContent(root);
        root.addStyleName("root");
        root.setSizeFull();

        Label bg = new Label();
        bg.setSizeUndefined();
        bg.addStyleName("login-bg");
        root.addComponent(bg);

        Boolean isLogged = (Boolean) getSession().getAttribute("is.logged");
        if(!enableSecurity || (isLogged != null && isLogged)) {
            securityManager = (ISecurityManager) getSession().getAttribute("security.manager");
            buildMainView();
        } else {
            buildLoginView(false);
        }
    }

    /**
     * Build login view
     * @param exit
     */
    private void buildLoginView(final boolean exit) {
        if (exit) {
            root.removeAllComponents();
        }
        notifierService.closeAll();

        addStyleName("login");

        loginLayout = new VerticalLayout();
        loginLayout.setId("webconsole_loginlayout_id");
        loginLayout.setSizeFull();
        loginLayout.addStyleName("login-layout");
        root.addComponent(loginLayout);

        final CssLayout loginPanel = new CssLayout();
        loginPanel.addStyleName("login-panel");

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        loginPanel.addComponent(labels);

        Label welcome = new Label("Welcome");
        welcome.addStyleName("h4");
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

        Label title = new Label(consoleName);
        //title.setSizeUndefined();
        title.addStyleName("h2");
        title.addStyleName("light");
        labels.addComponent(title);
        labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.setMargin(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Username");
        username.focus();
        username.setId("webconsole_login_username");
        fields.addComponent(username);

        final PasswordField password = new PasswordField("Password");
        password.setId("webconsole_login_password");
        fields.addComponent(password);

        final Button signin = new Button("Sign In");
        signin.setId("webconsole_login_signin");
        signin.addStyleName("default");
        fields.addComponent(signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        final ShortcutListener enter = new ShortcutListener("Sign In",
                ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                signin.click();
            }
        };

        signin.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Subject subject = authenticateService.authenticate(username.getValue(), password.getValue());
                if (subject != null) {
                    securityManager = new SecurityManager(subject);
                    getSession().setAttribute("is.logged", true);
                    getSession().setAttribute("security.manager", securityManager);
                    buildMainView();
                }
                else {
                    if (loginPanel.getComponentCount() > 2) {
                        // Remove the previous error message
                        loginPanel.removeComponent(loginPanel.getComponent(2));
                    }
                    // Add new error message
                    Label error = new Label(
                            "Wrong username or password.",
                            ContentMode.HTML);
                    error.setId("webconsole_login_error");
                    error.addStyleName("error");
                    error.setSizeUndefined();
                    error.addStyleName("light");
                    // Add animation
                    error.addStyleName("v-animate-reveal");
                    loginPanel.addComponent(error);
                    username.focus();
                }
            }
        });

        signin.addShortcutListener(enter);

        loginPanel.addComponent(fields);

        loginLayout.addComponent(loginPanel);
        loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
    }

    /**
     * Build main view
     */
    private void buildMainView() {
        getPage().setTitle(consoleName);
        menu = new CssLayout();
        menu.setId("webconsole_mainlayout_id");
        content = new CssLayout();
        //buildRoutes();

        notifierService.closeAll();

        // Build menu layout
        main = new HorizontalLayout() {
            {
                setSizeFull();
                addStyleName("main-view");
                addComponent(new VerticalLayout() {
                    // Sidebar
                    {
                        addStyleName("sidebar");
                        setWidth(null);
                        setHeight("100%");

                        // Branding element
                        addComponent(new CssLayout() {
                            {
                                addStyleName("branding");
                                Label logo = new Label(
                                        formatTitle(consoleName),
                                        ContentMode.HTML);
                                logo.setSizeUndefined();
                                addComponent(logo);
                            }
                        });

                        // Main menu
                        addComponent(menu);
                        setExpandRatio(menu, 1);

                        if (securityManager != null) {
                            // User menu
                            addComponent(new VerticalLayout() {
                                {
                                    setSizeUndefined();
                                    addStyleName("user");
                                    Image profilePic = new Image(
                                            null,
                                            new ThemeResource("img/profile-pic.png"));
                                    profilePic.setWidth("34px");
                                    addComponent(profilePic);
                                    Label userName = new Label(securityManager.getUserName());
                                    userName.setSizeUndefined();
                                    addComponent(userName);

                                    MenuBar.Command cmd = new MenuBar.Command() {
                                        @Override
                                        public void menuSelected(
                                                MenuBar.MenuItem selectedItem) {
                                            Notification
                                                    .show("Not implemented yet");
                                        }
                                    };
                                    MenuBar settings = new MenuBar();
                                    MenuBar.MenuItem settingsMenu = settings.addItem("",
                                            null);
                                    settingsMenu.setStyleName("icon-cog");
                                    settingsMenu.addItem("Settings", cmd);
                                    settingsMenu.addItem("Preferences", cmd);
                                    settingsMenu.addSeparator();
                                    settingsMenu.addItem("My Account", cmd);
                                    addComponent(settings);

                                    Button exit = new NativeButton("Exit");
                                    exit.addStyleName("icon-cancel");
                                    exit.setDescription("Sign Out");
                                    addComponent(exit);
                                    exit.addClickListener(new Button.ClickListener() {
                                        @Override
                                        public void buttonClick(Button.ClickEvent event) {
                                            ((SecurityManager) securityManager).setUserLogged(false);
                                            for (Map.Entry<ExtensionFactory, ScopeFactory> scopeFactoryEntry : scopesFactories.entrySet()) {
                                                ScopeFactory scopeFactory = scopeFactoryEntry.getValue();
                                                if (scopeFactory.getInstance() != null) {
                                                    scopeFactory.getInstance().stop();
                                                    scopeFactory.setInstance(null);
                                                }
                                            }
                                            nbScopesToBound = 0;
                                            progressIndicator.setValue(Float.valueOf(0));
                                            getSession().setAttribute("is.logged", false);
                                            buildLoginView(true);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

                // Content
                addComponent(content);
                content.setSizeFull();
                content.addStyleName("view-content");
                setExpandRatio(content, 1);
            }

        };
        nav = new Navigator(this, content);
        nav.addView("", new NavigatorView(new CssLayout()));
        nav.addView("/", new NavigatorView(new CssLayout()));

        menu.removeAllComponents();

        //Compute nb scopes to bound
        Map<ExtensionFactory, ScopeFactory> scopesToBound = new HashMap<>();
        for (Map.Entry<ExtensionFactory, ScopeFactory> scopeFactoryEntry : scopesFactories.entrySet()) {
            if (isAllowedToShowScope(scopeFactoryEntry.getValue().getRoles())) {
                scopesToBound.put(scopeFactoryEntry.getKey(), scopeFactoryEntry.getValue());
            }
        }
        nbScopesToBound = scopesToBound.size();

        // Tell scopesFactories view factories to create views
        for (Map.Entry<ExtensionFactory, ScopeFactory> scopeFactoryEntry : scopesToBound.entrySet()) {
            if (isAllowedToShowScope(scopeFactoryEntry.getValue().getRoles())) {
                ExtensionFactory extensionFactory = scopeFactoryEntry.getKey();
                ScopeFactory scopeFactory = scopeFactoryEntry.getValue();
                scopeFactory.setInstance(extensionFactory.create(new BaseUIContext(this, securityManager, uiId)));
            }
        }

        // Start progress indicator
        root.removeAllComponents();
        progressIndicatorLayout = new VerticalLayout();
        progressIndicatorLayout.setSizeFull();
        progressIndicatorLayout.addStyleName("login-layout");
        root.addComponent(progressIndicatorLayout);
        buildProgressIndicatorView();

        menu.addStyleName("menu");
        menu.setHeight("100%");

        nav.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                notifierService.closeAll();
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
            }
        });
        nav.navigateTo("/");
    }

    private void buildProgressIndicatorView() {

        final CssLayout progressPanel = new CssLayout();
        progressPanel.addStyleName("login-panel");

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        progressPanel.addComponent(labels);

        Label welcome = new Label("Welcome " + ((securityManager == null)? "":securityManager.getUserName()));
        welcome.addStyleName("h4");
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

        Label title = new Label(consoleName);
        title.addStyleName("h2");
        title.addStyleName("light");
        labels.addComponent(title);
        labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

        Float scopesViewsBound = (float) scopes.size();
        final Float stopValue = new Float(1.0);

        if (scopesFactories.isEmpty()) {
            progressIndicator.setValue(stopValue);
        } else {
            progressIndicator.setValue(scopesViewsBound/nbScopesToBound);
        }

        if (stopValue.equals(progressIndicator.getValue())) {
            showMainContent();
        } else {
//            TimeOutThread timeOutThread = new TimeOutThread();
//            timeOutThread.start();
            progressIndicator.setPollingInterval(500);
            progressIndicator.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    if (stopValue.equals(event.getProperty().getValue())) {
                        showMainContent();
                    }
                }
            });
        }

        HorizontalLayout progressBarPanel = new HorizontalLayout();
        progressBarPanel.setWidth("100%");
        progressBarPanel.setMargin(true);
        progressBarPanel.addComponent(progressIndicator);
        progressBarPanel.setComponentAlignment(progressIndicator, Alignment.MIDDLE_CENTER);
        progressPanel.addComponent(progressBarPanel);

        progressIndicatorLayout.addComponent(progressPanel);
        progressIndicatorLayout.setComponentAlignment(progressPanel, Alignment.MIDDLE_CENTER);
    }

    protected void showMainContent() {
        removeStyleName("login");
        root.removeAllComponents();
        root.addComponent(main);
    }

    /**
     * Format console title
     * @param title
     * @return
     */
    private String formatTitle(String title) {
        String[] words = title.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append("<center><span>");
        for (int i = 0; i < words.length; i++) {
            sb.append(words[i]);
            sb.append("<br />");
        }
        sb.append("</span></center>");
        return sb.toString();
    }

    /**
     * Clear menu selection
     */
    private void clearMenuSelection() {
        for (Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
            Component next = it.next();
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            } else if (next instanceof DragAndDropWrapper) {
                // Wow, this is ugly (even uglier than the rest of the code)
                ((DragAndDropWrapper) next).iterator().next()
                        .removeStyleName("selected");
            }
        }
    }

    /**
     * Add route for scope view to navigator
     * @param scopeView
     */
    private void addRouteToNav(String scopeName, Component scopeView) {
        if (nav != null) {
            nav.removeView("/" + scopeName);

            View view;
            try {
                view = new NavigatorView(scopeView);
            } catch (Exception e) {
                view = new NavigatorView(new ExceptionView(e));
            }
            nav.addView("/" + scopeName, view);

            // TODO change 'test' by 'home' when home scope one is available
            if ("test".equals(scopeName.toLowerCase())) {
                nav.addView("", view);
                nav.addView("/", view);
            }
        }
    }

    /**
     * Remove route for scope view from navigator
     * @param scopeName
     */
    private void removeRouteFromNav(String scopeName) {
        if (nav != null) {
            nav.removeView("/" + scopeName);
            // TODO change 'test' by 'home' when home scope one is available
            if ("test".equals(scopeName)) {
                nav.removeView("");
                nav.removeView("/");
            }
        }
    }

    /**
     * Add scope button in menu
     * @param scopeName
     * @param notify for notifierService to show badge
     */
    private void addScopeButtonInMenu(final String scopeName, final Component scopeView, boolean notify) {
        if (menu != null) {
            final Button b = new NativeButton(scopeName.toUpperCase());

            b.addStyleName("icon-dashboard");

            b.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    clearMenuSelection();
                    notifierService.removeBadge(scopeView);
                    event.getButton().addStyleName("selected");
                    nav.navigateTo("/" + scopeName);
                }
            });

            access(new Runnable() {
                @Override
                public void run() {
                    menu.addComponent(b);
                }
            });

            notifierService.addScopeButton(scopeView, b, this, notify);

            scopes.get(scopeName).setScopeMenuButton(b);

            if (nbScopesToBound > 0) {
                Float progressIndicatorValue = progressIndicator.getValue();
                progressIndicatorValue += (float) (1.0 / nbScopesToBound);
                progressIndicator.setValue(progressIndicatorValue);
            }
        }
    }

    /**
     * Remove scope button from menu
     * @param scopeName
     */
    private void removeScopeButtonInMenu(final String scopeName) {
        if (scopes.get(scopeName).getScopeMenuButton() != null) {
            access(new Runnable() {
                @Override
                public void run() {
                    menu.removeComponent(scopes.get(scopeName).getScopeMenuButton());
                }
            });
            scopes.get(scopeName).setScopeMenuButton(null);
            notifierService.removeScopeButton((Component) scopes.get(scopeName).getScopeView());
        }
    }

    private boolean isAllowedToShowScope(String[] rolesAllowed) {
        if (securityManager == null) return true;
        return securityManager.isUserInRoles(rolesAllowed);
    }

    @Override
    public void access(Runnable runnable) throws UIDetachedException {
        Map<Class<?>, CurrentInstance> old = null;

        VaadinSession session = getSession();

        if (session == null) {
            throw new UIDetachedException();
        }

        // TODO hack to avoid exception when another session had lock
        // TODO PGWK-7
        //VaadinService.verifyNoOtherSessionLocked(session);

        session.lock();
        try {
            if (getSession() == null) {
                // UI was detached after fetching the session but before we
                // acquired the lock.
                throw new UIDetachedException();
            }
            old = CurrentInstance.setThreadLocals(this);
            runnable.run();
        } finally {
            session.unlock();
            if (old != null) {
                CurrentInstance.restoreThreadLocals(old);
            }
        }
    }

    public class TimeOutThread extends Thread {

        @Override
        public void run() {
            try {
                sleep(3000);
                if (progressIndicator.getValue() < 1) {
                    access(new Runnable() {
                        @Override
                        public void run() {
                            showMainContent();
                        }
                    });
                }
            } catch (InterruptedException e) {}
        }
    }
}
