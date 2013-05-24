package com.peergreen.webconsole.core.api;

import com.peergreen.webconsole.core.notifier.NotificationOverlay;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 17/05/13
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */
public interface INotifierService {

    NotificationOverlay addOverlay(String caption, String text, String style);

    void addScopeButton(View scope, Button button, boolean notify);

    void removeScopeButton(View scope);

    void hideScopeButton(View scope);

    void removeBadge(View scope);

    void incrementBadge(View scope);

    void decrementBadge(View scope);

    void closeAll();

    void addNotification(String notification);
}
