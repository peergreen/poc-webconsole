package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.core.api.IHelpManager;
import com.peergreen.webconsole.core.api.IScopeProvider;
import com.peergreen.webconsole.core.api.IVaadinUI;
import com.peergreen.webconsole.core.scopes.ScopeExceptionView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 07/05/13
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */

@Theme("dashboard")
@Title("Peergreen Administration Console")
//@Push
public class MainUI extends UI implements IVaadinUI {

    CssLayout root = new CssLayout();

    VerticalLayout loginLayout;

    CssLayout menu = new CssLayout();

    CssLayout content = new CssLayout();

    private static final long serialVersionUID = -7333618595654346783L;

    private IHelpManager helpManager;

    private Navigator nav;

    private HashMap<String, IScopeProvider> scopes = new HashMap<>();

    private HashMap<String, Button> viewNameToMenuButton = new HashMap<>();

    private String selectedView = "";

    private String mainScope = "";

    private BadRequestView badRequestView;

    public MainUI(List<IScopeProvider> scopes, IHelpManager helpManager, String mainScope) {
        this.helpManager = helpManager;
        this.mainScope = mainScope;
        for (IScopeProvider scope : scopes) {
            this.scopes.put(scope.getName(), scope);
        }
        badRequestView = new BadRequestView();
    }

    private List<String> buttonStyles = new ArrayList<String>() {{
        add("icon-dashboard");
        add("icon-sales");
        add("icon-schedule");
        add("icon-reports");
        add("icon-transactions");
    }};

    @Override
    protected void init(final VaadinRequest request) {

        setLocale(Locale.US);

        setContent(root);
        root.addStyleName("root");
        root.setSizeFull();

        Label bg = new Label();
        bg.setSizeUndefined();
        bg.addStyleName("login-bg");
        root.addComponent(bg);

        buildLoginView(false);

    }

    private void buildLoginView(final boolean exit) {
        if (exit) {
            root.removeAllComponents();
        }
        helpManager.closeAll();
        HelpOverlay w = helpManager
                .addOverlay(
                        "",
                        "Welcome to Peergreen Administration Console",
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

        Label title = new Label("Administration Console");
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
                buildMainView();
//                if (username.getValue() != null
//                        && username.getValue().equals("")
//                        && password.getValue() != null
//                        && password.getValue().equals("")) {
//                    signin.removeShortcutListener(enter);
//                    buildMainView();
//                } else {
//                    if (loginPanel.getComponentCount() > 2) {
//                        // Remove the previous error message
//                        loginPanel.removeComponent(loginPanel.getComponent(2));
//                    }
//                    // Add new error message
//                    Label error = new Label(
//                            "Wrong username or password. <span>Hint: try empty values</span>",
//                            ContentMode.HTML);
//                    error.addStyleName("error");
//                    error.setSizeUndefined();
//                    error.addStyleName("light");
//                    // Add animation
//                    error.addStyleName("v-animate-reveal");
//                    loginPanel.addComponent(error);
//                    username.focus();
//                }
            }
        });

        signin.addShortcutListener(enter);

        loginPanel.addComponent(fields);

        loginLayout.addComponent(loginPanel);
        loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
    }

    private void buildMainView() {

        buildRoutes();

        helpManager.closeAll();
        removeStyleName("login");
        root.removeComponent(loginLayout);

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
                                        "<span>Administration</span> Console",
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
                                Label userName = new Label("Mohammed"
                                        + " "
                                        + "Boukada");
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

        for(final Map.Entry<String, IScopeProvider> scope : scopes.entrySet()) {
            addScopeButtonInMenu(scope.getKey());
        }

        menu.addStyleName("menu");
        menu.setHeight("100%");

        String f = Page.getCurrent().getUriFragment();
        if (f != null && f.startsWith("!")) {
            f = f.substring(1);
        }
        if (f == null || f.equals("") || f.equals("/")) {
            nav.navigateTo("/" + mainScope);
            viewNameToMenuButton.get("/" + mainScope).addStyleName("selected");
            selectedView = "/" + mainScope;
        } else if (viewNameToMenuButton.containsKey(f)) {
            nav.navigateTo(f);
            viewNameToMenuButton.get(f).addStyleName("selected");
            selectedView = f;
        } else {
            badRequestView.setBadRequest(f.substring(1));
            nav.navigateTo("/badrequest");
        }

        nav.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                helpManager.closeAll();
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
            }
        });
    }

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

    private void buildRoutes() {
        nav = new Navigator(this, content);
        // Build routes
        for (Map.Entry<String, IScopeProvider> scope : scopes.entrySet()) {
            addRouteToNav(scope.getValue());
        }
        nav.addView("/badrequest", badRequestView);
    }

    private void addRouteToNav(IScopeProvider scope) {
        if (nav != null) {
            try {
                nav.removeView("/" + scope.getName());
                nav.addView("/" + scope.getName(), scope.getView());
            } catch (Exception e) {
                nav.addView("/" + scope.getName(), new ScopeExceptionView(e));
            }
        }
    }

    private void removeRouteFromNav(IScopeProvider scope) {
        if (nav != null) {
            nav.removeView("/" + scope.getName());
        }
    }

    @Override
    public void addScope(IScopeProvider scope) {
        scopes.put(scope.getName(), scope);

        addRouteToNav(scope);

        updateScopeMenuButton(scope.getName());

        updateScopeBadge(scope.getName());
    }

    @Override
    public void removeScope(IScopeProvider scope) {
        scopes.remove(scope.getName());

        removeRouteFromNav(scope);

        updateScopeMenuButton(scope.getName());
    }

    @Override
    public void setMainScope(String scopeName) {
        mainScope = scopeName;
    }

    public void updateScopeMenuButton(String scopeName) {

        if (viewNameToMenuButton.containsKey("/" + scopeName)) {
            if (!scopes.containsKey(scopeName)) {
                // this scope was removed, remove its button from menu
                menu.removeComponent(viewNameToMenuButton.get("/" + scopeName));
                viewNameToMenuButton.remove("/" + scopeName);
                return; // exit this method to avoid following tests
            }
            if (scopes.get(scopeName).getViewsInScope().isEmpty()) {
                // this scope is empty, hide it
                viewNameToMenuButton.get("/" + scopeName).setVisible(false);
            }
            if (!viewNameToMenuButton.get("/" + scopeName).isVisible()
                    && !scopes.get(scopeName).getViewsInScope().isEmpty()) {
                // a view was added to a hidden scope, show it
                viewNameToMenuButton.get("/" + scopeName).setVisible(true);
            }
        } else {
            // new scope
            addScopeButtonInMenu(scopeName);
        }
    }

    private void addScopeButtonInMenu(final String scopeName) {
        if (!scopes.containsKey(scopeName)) {
            return;
        }

        final Button b = new NativeButton(scopeName.toUpperCase());

        if (mainScope.equals(scopeName)) {
            b.addStyleName("icon-dashboard");
        } else {
            String style = buttonStyles.get(new Random().nextInt(buttonStyles.size()));
            b.addStyleName(style);
        }

        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                clearMenuSelection();
                removeScopeBadge(scopeName);
                scopes.get(scopeName).setBadge(0);
                event.getButton().addStyleName("selected");
                selectedView = "/" + scopeName;
                if (!nav.getState().equals("/" + scopeName))
                    nav.navigateTo("/" + scopeName);
            }
        });

        menu.addComponent(b);

        if (!mainScope.equals(scopeName) && scopes.get(scopeName).getViewsInScope().isEmpty()) {
            b.setVisible(false);
        }

        viewNameToMenuButton.put("/" + scopeName, b);
    }

    public void updateScopeBadge(String scopeName) {
        if (!viewNameToMenuButton.containsKey("/" + scopeName)) {
            return;
        }

        if (!selectedView.equals("/" + scopeName)) {
            viewNameToMenuButton.get("/" + scopeName).setHtmlContentAllowed(true);
            viewNameToMenuButton.get("/" + scopeName).setCaption(scopeName.toUpperCase() +
                                ((scopes.get(scopeName).getBadge() <= 0)?"":"<span class=\"badge\">" +
                                scopes.get(scopeName).getBadge() +"</span>"));
        }
    }

    private void removeScopeBadge(String scopeName) {
        viewNameToMenuButton.get("/" + scopeName).setHtmlContentAllowed(true);
        viewNameToMenuButton.get("/" + scopeName).setCaption(scopeName.toUpperCase());
    }

}