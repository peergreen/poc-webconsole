package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.security.UsernamePasswordAuthenticateService;
import com.peergreen.webconsole.core.api.INotifierService;
import com.peergreen.webconsole.core.api.IScopeFactory;
import com.peergreen.webconsole.core.api.ISecurityManager;
import com.peergreen.webconsole.core.exception.ExceptionView;
import com.peergreen.webconsole.core.notifier.NotificationOverlay;
import com.peergreen.webconsole.core.scope.HomeScope;
import com.peergreen.webconsole.core.scope.ScopeNavView;
import com.peergreen.webconsole.core.security.SecurityManager;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;

import javax.security.auth.Subject;
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
@Provides(specifications = BaseUI.class)
public class BaseUI extends UI {

    /**
     * Root layout
     */
    CssLayout root = new CssLayout();

    /**
     * Login lyout
     */
    VerticalLayout loginLayout;

    /**
     * Menu layout
     */
    CssLayout menu = new CssLayout();

    /**
     * Content layout
     */
    CssLayout content = new CssLayout();

    /**
     * To navigate between scopes views
     */
    private Navigator nav;

    /**
     * Scopes bound
     */
    private ConcurrentHashMap<String, IScopeFactory> scopes = new ConcurrentHashMap<>();

    /**
     * Scopes views
     */
    private ConcurrentHashMap<String, com.vaadin.ui.Component> scopesViews = new ConcurrentHashMap<>();

    /**
     * Buttons related name
     */
    private HashMap<String, Button> viewNameToMenuButton = new HashMap<>();

    /**
     * Console name
      */
    private String consoleName;

    /**
     * Whether the UI was built
     */
    private boolean uiIsBuilt = false;

    /**
     * Security manager
     */
    private ISecurityManager securityManager;

    /**
     * Notifier service
     */
    @Requires
    private INotifierService notifierService;

    @Requires
    private UsernamePasswordAuthenticateService authenticateService;

    /**
     * Base console UI constructor
     * @param consoleName
     */
    public BaseUI(String consoleName) {
        this.consoleName = consoleName;
        this.securityManager = new SecurityManager();
        VaadinSession.getCurrent().setAttribute("security.manager", securityManager);
    }

    /**
     * Bind a scope factory
     * @param scope
     */
    @Bind(aggregate = true, optional = true)
    public void bindScope(IScopeFactory scope) {
        scopes.put(scope.getSymbolicName(), scope);
        addRouteToNav(scope);
        addScopeButtonInMenu(scope, true);
    }

    /**
     * Unbind a scope factory
     * @param scope
     */
    @Unbind
    public void unbindScope(IScopeFactory scope) {
        if (scopes.containsKey(scope.getSymbolicName())) {
            removeRouteFromNav(scope);
            removeScopeButtonInMenu(scope);
            scopes.remove(scope.getSymbolicName());
            scopesViews.remove(scope.getSymbolicName());
        }
    }

    /**
     * Init UI
     * @param request
     */
    @Override
    protected void init(VaadinRequest request) {
        setLocale(Locale.US);

        getPage().setTitle(consoleName);
        setContent(root);
        root.addStyleName("root");
        root.setSizeFull();

        Label bg = new Label();
        bg.setSizeUndefined();
        bg.addStyleName("login-bg");
        root.addComponent(bg);

        buildLoginView(false);
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
        NotificationOverlay w = notifierService
                .addOverlay(
                        "",
                        "Welcome to " + consoleName,
                        "login");
        w.center();
        addWindow(w);

        addStyleName("login");

        loginLayout = new VerticalLayout();
        loginLayout.setSizeFull();
        loginLayout.addStyleName("login-layout");
        root.addComponent(loginLayout);

        final CssLayout loginPanel = new CssLayout();
        loginPanel.addStyleName("login-panel");

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        labels.addStyleName("labels");
        loginPanel.addComponent(labels);

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName("h4");
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

        Label title = new Label(consoleName);
        title.setSizeUndefined();
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
        fields.addComponent(username);

        final PasswordField password = new PasswordField("Password");
        fields.addComponent(password);

        final Button signin = new Button("Sign In");
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
                    ((SecurityManager) securityManager).setSubject(subject);
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
        if (!securityManager.isUserLogged()) {
            buildLoginView(true);
        }

        uiIsBuilt = true;
        buildRoutes();

        notifierService.closeAll();
        removeStyleName("login");
        root.removeComponent(loginLayout);

        // Build menu layout
        root.addComponent(new HorizontalLayout() {
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
                                        buildLoginView(true);
                                    }
                                });
                            }
                        });
                    }
                });

                // Content
                addComponent(content);
                content.setSizeFull();
                content.addStyleName("view-content");
                setExpandRatio(content, 1);
            }

        });

        menu.removeAllComponents();

        // Add Menu buttons
        for(final Map.Entry<String, IScopeFactory> scope : scopes.entrySet()) {
            if (showScope(scope.getValue())) {
                addScopeButtonInMenu(scope.getValue(), false);
            }
        }

        menu.addStyleName("menu");
        menu.setHeight("100%");

        String f = Page.getCurrent().getUriFragment();
        if (f != null && f.startsWith("!")) {
            f = f.substring(1);
        }
        if (f == null || f.equals("") || f.equals("/")) {
            nav.navigateTo("/home");
            viewNameToMenuButton.get("/home").addStyleName("selected");
        } else {
            nav.navigateTo(f);
            viewNameToMenuButton.get(f).addStyleName("selected");
        }

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
     * Build navigator routes to scopes views
     */
    private void buildRoutes() {
        nav = new Navigator(this, content);
        // Build routes
        for (Map.Entry<String, IScopeFactory> scope : scopes.entrySet()) {
            if (showScope(scope.getValue())) {
                addRouteToNav(scope.getValue());
            }
        }
    }

    /**
     * Add route for scope view to navigator
     * @param scope
     */
    private void addRouteToNav(IScopeFactory scope) {
        if (nav != null) {
            try {
                nav.removeView("/" + scope.getSymbolicName());
                try {
                    scopesViews.put(scope.getSymbolicName(), scope.getView());
                } catch (Exception e) {
                    scopesViews.put(scope.getSymbolicName(), new ExceptionView(e));
                }
                View view = new ScopeNavView(scopesViews.get(scope.getSymbolicName()));
                nav.addView("/" + scope.getSymbolicName(), view);
                // If is home scope
                // attach the view to empty and "/" routes
                if (HomeScope.SCOPE_NAME.equals(scope.getSymbolicName())) {
                    nav.addView("", view);
                    nav.addView("/", view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Remove route for scope view from navigator
     * @param scope
     */
    private void removeRouteFromNav(IScopeFactory scope) {
        if (nav != null) {
            nav.removeView("/" + scope.getSymbolicName());
        }
    }

    /**
     * Add scope button in menu
     * @param scope
     * @param notify for notifierService to show badge
     */
    private void addScopeButtonInMenu(final IScopeFactory scope, boolean notify) {

        if (!uiIsBuilt) {
            return;
        }

        final Button b = new NativeButton(scope.getSymbolicName().toUpperCase());

        b.addStyleName(scope.getStyle());

        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                clearMenuSelection();
                notifierService.removeBadge(scopesViews.get(scope.getSymbolicName()));
                event.getButton().addStyleName("selected");
                if (!nav.getState().equals("/" + scope.getSymbolicName()))
                    nav.navigateTo("/" + scope.getSymbolicName());
            }
        });
        menu.getUI().getSession().getLockInstance().lock();
        try {
            menu.addComponent(b);
        } finally {
            menu.getUI().getSession().getLockInstance().unlock();
        }

        notifierService.addScopeButton(scopesViews.get(scope.getSymbolicName()), b, notify);

        viewNameToMenuButton.put("/" + scope.getSymbolicName(), b);
    }

    /**
     * Remove scope button from menu
     * @param scope
     */
    private void removeScopeButtonInMenu(IScopeFactory scope) {
        if (viewNameToMenuButton.containsKey("/" + scope.getSymbolicName())) {
            menu.removeComponent(viewNameToMenuButton.get("/" + scope.getSymbolicName()));
            viewNameToMenuButton.remove("/" + scope.getSymbolicName());
            notifierService.removeScopeButton(scopesViews.get(scope.getSymbolicName()));
        }
    }

    private boolean showScope(IScopeFactory scopeFactory) {
        return securityManager.isUserInRoles(scopeFactory.getAllowedRoles());
    }
}
