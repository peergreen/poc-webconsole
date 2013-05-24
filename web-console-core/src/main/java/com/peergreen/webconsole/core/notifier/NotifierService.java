/**
 * DISCLAIMER
 *
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 *
 * @author jouni@vaadin.com
 *
 */

package com.peergreen.webconsole.core.notifier;

import com.peergreen.webconsole.core.api.INotifierService;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Notifier service
 * @author Mohammed Boukada
 */
@Component
@Provides
@Instantiate
public class NotifierService implements INotifierService {

    /**
     * List of overlays
     */
    private List<NotificationOverlay> overlays = new ArrayList<>();

    /**
     * Badges for each scope button
     */
    private ConcurrentHashMap<View, Integer> badges = new ConcurrentHashMap<>();

    /**
     * Scope buttons in each view
     */
    private ConcurrentHashMap<View, Button> scopesButtons = new ConcurrentHashMap<>();

    /**
     * Close all overlays
     */
    public void closeAll() {
        for (NotificationOverlay overlay : overlays) {
            overlay.close();
        }
        overlays.clear();
    }

    /** {@inheritDoc}
     */
    @Override
    public void addNotification(String notification) {
        Notification.show(notification);
    }

    /** {@inheritDoc}
     */
    public NotificationOverlay addOverlay(String caption, String text, String style) {
        NotificationOverlay o = new NotificationOverlay();
        o.setCaption(caption);
        o.addComponent(new Label(text, ContentMode.HTML));
        o.setStyleName(style);
        overlays.add(o);
        return o;
    }

    /** {@inheritDoc}
     */
    public void addScopeButton(View scope, Button button, boolean notify) {
        scopesButtons.put(scope, button);
        badges.put(scope, 0);
        if (notify) {
            setBadgeAsNew(button);
        }
    }

    /** {@inheritDoc}
     */
    public void removeScopeButton(View scope) {
        if (scopesButtons.containsKey(scope)) {
            scopesButtons.remove(scope);
            badges.remove(scope);
        }
    }

    /** {@inheritDoc}
     */
    public void hideScopeButton(View scope) {
        if (scopesButtons.containsKey(scope)) {
            scopesButtons.get(scope).setVisible(false);
        }
    }

    /** {@inheritDoc}
     */
    public void removeBadge(View scope) {
        updateBadge(scope, 0);
        scopesButtons.get(scope).setHtmlContentAllowed(true);
        scopesButtons.get(scope).setCaption(getInitialCaption(scopesButtons.get(scope)));
    }

    /** {@inheritDoc}
     */
    public void incrementBadge(View scope) {
        if (scopesButtons.containsKey(scope)) {
            updateBadge(scope, +1);
            scopesButtons.get(scope).setVisible(true);
            scopesButtons.get(scope).setHtmlContentAllowed(true);
            String newCaption = getInitialCaption(scopesButtons.get(scope)) +
                    "<span class=\"badge\">" + badges.get(scope) +"</span>";
            scopesButtons.get(scope).setCaption(newCaption);
        }
    }

    /** {@inheritDoc}
     */
    public void decrementBadge(View scope) {
        if (scopesButtons.containsKey(scope)) {
            updateBadge(scope, -1);
            scopesButtons.get(scope).setHtmlContentAllowed(true);
            String newCaption = getInitialCaption(scopesButtons.get(scope)) +
                    ((badges.get(scope) == 0) ? "" : "<span class=\"badge\">" + badges.get(scope) +"</span>");
            scopesButtons.get(scope).setCaption(newCaption);
        }
    }

    /**
     * Set badge as new
     * @param button
     */
    private void setBadgeAsNew(Button button) {
        button.setHtmlContentAllowed(true);
        String newCaption = getInitialCaption(button) +
                "<span class=\"badge\">new</span>";
        button.setCaption(newCaption);
    }

    /**
     * Update badge when it is changed
     * @param scope
     * @param op
     */
    private void updateBadge(View scope, int op) {
        if (badges.containsKey(scope)) {
            Integer badge = badges.get(scope);
            if (op == 0) {
                badge = 0;
            } else if (op == +1) {
                badge++;
            } else if (op == -1 && badge > 0) {
                badge--;
            }
            badges.put(scope, badge);
        }
    }

    /**
     * Get initial caption of the button
     * @param button
     * @return
     */
    private String getInitialCaption(Button button) {
        if (button.getCaption().indexOf("<span") != -1) {
            return button.getCaption().substring(0, button.getCaption().indexOf("<span"));
        }
        return button.getCaption();
    }

}
