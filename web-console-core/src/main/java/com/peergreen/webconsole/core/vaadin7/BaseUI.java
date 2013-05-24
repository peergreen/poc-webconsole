package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.core.api.INotifierService;
import com.peergreen.webconsole.core.api.IScopeFactory;
import com.peergreen.webconsole.core.notifier.NotificationOverlay;
import com.peergreen.webconsole.core.scope.DefaultScope;
import com.peergreen.webconsole.core.scope.HomeScope;
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
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 22/05/13
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 */
@Theme("dashboard")
@PreserveOnRefresh
@org.apache.felix.ipojo.annotations.Component
@Provides(specifications = BaseUI.class)
public class BaseUI extends UI {

    CssLayout root = new CssLayout();

    VerticalLayout loginLayout;

    CssLayout menu = new CssLayout();

    CssLayout content = new CssLayout();

    private Navigator nav;

    private ConcurrentHashMap<String, IScopeFactory> scopes = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, View> scopesViews = new ConcurrentHashMap<>();

    private HashMap<String, Button> viewNameToMenuButton = new HashMap<>();

    private String consoleName;

    private boolean uiIsBuilt = false;

    @Requires
    INotifierService notifierService;

    public BaseUI(String consoleName) {
        this.consoleName = consoleName;
    }

    @Invalidate
    public void stop() {
        System.out.println("I'm stopping");
    }

    @Bind(aggregate = true, optional = true)
    public void bindScope(IScopeFactory scope) {
        scopes.put(scope.getName(), scope);
        scopesViews.put(scope.getName(), scope.getView());
        addRouteToNav(scope);
        addScopeButtonInMenu(scope, true);
    }

    @Unbind
    public void unbindScope(IScopeFactory scope) {
        removeRouteFromNav(scope);
        removeScopeButtonInMenu(scope);
        scopes.remove(scope.getName());
        scopesViews.remove(scope.getName());
    }

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

        uiIsBuilt = true;
        buildRoutes();

        notifierService.closeAll();
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

        // Add Menu buttons
        for(final Map.Entry<String, IScopeFactory> scope : scopes.entrySet()) {
            addScopeButtonInMenu(scope.getValue(), false);
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
        for (Map.Entry<String, IScopeFactory> scope : scopes.entrySet()) {
            addRouteToNav(scope.getValue());
        }
    }

    private void addRouteToNav(IScopeFactory scope) {
        if (nav != null) {
            try {
                nav.removeView("/" + scope.getName());
                nav.addView("/" + scope.getName(), scopesViews.get(scope.getName()));
                if (DefaultScope.SCOPE_NAME.equals(scope.getName())) {
                    nav.addView("", scopesViews.get(scope.getName()));
                    nav.addView("/", scopesViews.get(scope.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void removeRouteFromNav(IScopeFactory scope) {
        if (nav != null) {
            nav.removeView("/" + scope.getName());
        }
    }

    private void addScopeButtonInMenu(final IScopeFactory scope, boolean notify) {

        if (!uiIsBuilt) {
            return;
        }

        final Button b = new NativeButton(scope.getName().toUpperCase());

        b.addStyleName(scope.getStyle());

        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                clearMenuSelection();
                notifierService.removeBadge(scopesViews.get(scope.getName()));
                event.getButton().addStyleName("selected");
                if (!nav.getState().equals("/" + scope.getName()))
                    nav.navigateTo("/" + scope.getName());
            }
        });
        menu.getUI().getSession().getLockInstance().lock();
        try {
            menu.addComponent(b);
        } finally {
            menu.getUI().getSession().getLockInstance().unlock();
        }

        notifierService.addScopeButton(scopesViews.get(scope.getName()), b, notify);

        viewNameToMenuButton.put("/" + scope.getName(), b);
    }

    private void removeScopeButtonInMenu(IScopeFactory scope) {
        if (viewNameToMenuButton.containsKey("/" + scope.getName())) {
            //menu.getUI().getSession().getLockInstance().lock();
            try {
                menu.removeComponent(viewNameToMenuButton.get("/" + scope.getName()));
            } finally {
                //menu.getUI().getSession().getLockInstance().unlock();
            }
            viewNameToMenuButton.remove("/" + scope.getName());
            notifierService.removeScopeButton(scopesViews.get(scope.getName()));
        }
    }
}