package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.core.api.IVaadinUI;
import com.peergreen.webconsole.core.api.IViewContribution;
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

    private HelpManager helpManager;

    private Navigator nav;

    private HashMap<String, List<IViewContribution>> scopes = new HashMap<>();

    private HashMap<String, AbstractScopeView> scopesViews = new HashMap<>();

    private HashMap<String, Button> viewNameToMenuButton = new HashMap<>();

    private List<IViewContribution> views;

    public MainUI(List<IViewContribution> views) {
        this.views = views;
    }


    @Override
    protected void init(final VaadinRequest request) {

        helpManager = new HelpManager(this);

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

        computeScopes();
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

        for(final Map.Entry<String, List<IViewContribution>> scope : scopes.entrySet()) {
            addScopeButtonInMenu(scope.getKey(), scope.getValue());
        }

        menu.addStyleName("menu");
        menu.setHeight("100%");

        String f = Page.getCurrent().getUriFragment();
        if (f != null && f.startsWith("!")) {
            f = f.substring(1);
        }
        if (f == null || f.equals("") || f.equals("/")) {
            nav.navigateTo("/index");
            //menu.getComponent(0).addStyleName("selected");
        } else {
            nav.navigateTo(f);
            viewNameToMenuButton.get(f).addStyleName("selected");
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

    private void computeScopes() {
        scopes.clear();
        // Compute scopes
        for(IViewContribution view : views) {
            String scope = view.getScope();
            List<IViewContribution> list;
            if (scopes.get(scope) == null) {
                list = new ArrayList();
                list.add(view);
                scopes.put(scope, list);
            } else {
                list = scopes.get(scope);
                if (!list.contains(view)) {
                    list.add(view);
                }
            }
        }
    }

    private void buildRoutes() {
        // Build routes
        nav = new Navigator(this, content);
        nav.addView("/index", DefaultView.class);
        for (Map.Entry<String, List<IViewContribution>> scope : scopes.entrySet()) {
            addScopeView(scope.getKey(), scope.getValue());
        }
    }

    @Override
    public void addView(IViewContribution view) {
        views.add(view);
        if (!scopes.containsKey(view.getScope())) {
            addScope(view.getScope());
        }

        scopes.get(view.getScope()).add(view);
        scopesViews.get(view.getScope()).addView(view);
    }

    @Override
    public void removeView(IViewContribution view) {
        views.remove(view);
        scopesViews.get(view.getScope()).removeView(view);
        scopes.get(view.getScope()).remove(view);

        if (scopes.get(view.getScope()).isEmpty()) {
            removeScope(view.getScope());
        }
    }

    @Override
    public HelpManager getHelpManager() {
        return helpManager;
    }

    private void addScope(String scopeName) {
        addScope(scopeName, new ArrayList<IViewContribution>());
    }

    private void addScope(String scopeName, List<IViewContribution> modules) {
        scopes.put(scopeName, modules);
        addScopeView(scopeName);
        addScopeButtonInMenu(scopeName, modules);
    }

    private void removeScope(String scopeName) {
        scopes.remove(scopeName);
        removeScopeView(scopeName);
        removeScopeButtonInMenu(scopeName);
    }

    private void addScopeView(String scopeName) {
        addScopeView(scopeName, new ArrayList<IViewContribution>());
    }

    private void addScopeView(String scopeName, List<IViewContribution> modules) {
        scopesViews.put(scopeName, new AbstractScopeView(modules, this));
        nav.removeView("/" + scopeName);
        nav.addView("/" + scopeName, scopesViews.get(scopeName));
    }

    private void removeScopeView(String scopeName) {
        scopesViews.remove(scopeName);
        nav.removeView("/" + scopeName);
    }

    private void addScopeButtonInMenu(final String scopeName, List<IViewContribution> modules) {
        Button b = new NativeButton(scopeName.substring(0, 1).toUpperCase()
                + scopeName.substring(1).replace('-', ' '));
        b.addStyleName("icon-dashboard");

        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                clearMenuSelection();
                event.getButton().addStyleName("selected");
                if (!nav.getState().equals("/" + scopeName))
                    nav.navigateTo("/" + scopeName);
            }
        });

        menu.addComponent(b);

        viewNameToMenuButton.put("/" + scopeName, b);
    }

    private void removeScopeButtonInMenu(String scopeName) {
        menu.removeComponent(viewNameToMenuButton.get("/" + scopeName));
        viewNameToMenuButton.remove("/" + scopeName);
    }

}
