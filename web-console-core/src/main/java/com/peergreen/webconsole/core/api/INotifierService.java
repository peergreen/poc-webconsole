package com.peergreen.webconsole.core.api;

import com.peergreen.webconsole.core.notifier.NotificationOverlay;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;

/**
 * Notifier service
 * @author Mohammed Boukada
 */
public interface INotifierService {

    /**
     * Add an overlay
     * @param caption
     * @param text
     * @param style
     * @return
     */
    NotificationOverlay addOverlay(String caption, String text, String style);

    /**
     * Add scope button reference
     * @param scope
     * @param button
     * @param notify
     */
    void addScopeButton(View scope, Button button, boolean notify);

    /**
     * Remove scope button reference
     * @param scope
     */
    void removeScopeButton(View scope);

    /**
     * Hide scope button from menu
     * @param scope
     */
    void hideScopeButton(View scope);

    /**
     * Remove badge from scope button in menu
     * @param scope
     */
    void removeBadge(View scope);

    /**
     * Increment badge in scope button in menu
     * @param scope
     */
    void incrementBadge(View scope);

    /**
     * Decrement badge in scope button in menu
     * @param scope
     */
    void decrementBadge(View scope);

    /**
     * Close all overlays
     */
    void closeAll();

    /**
     * Add a notification
     * @param notification
     */
    void addNotification(String notification);
}
