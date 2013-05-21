package com.peergreen.webconsole.core.api;

import com.peergreen.webconsole.core.vaadin7.HelpOverlay;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 17/05/13
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */
public interface IHelpManager {

    HelpOverlay addOverlay(String caption, String text, String style);

    void closeAll();
}
