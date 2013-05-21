/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.peergreen.webconsole.core.vaadin7;

import com.peergreen.webconsole.core.api.IHelpManager;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import java.util.ArrayList;
import java.util.List;

@Component
@Provides
@Instantiate
public class HelpManager implements IHelpManager {

    private List<HelpOverlay> overlays = new ArrayList<HelpOverlay>();


    public void closeAll() {
        for (HelpOverlay overlay : overlays) {
            overlay.close();
        }
        overlays.clear();
    }

    public void showHelpFor(View view) {
    }

    public void showHelpFor(Class<? extends View> view) {

    }

    public HelpOverlay addOverlay(String caption, String text, String style) {
        HelpOverlay o = new HelpOverlay();
        o.setCaption(caption);
        o.addComponent(new Label(text, ContentMode.HTML));
        o.setStyleName(style);
        overlays.add(o);
        return o;
    }

}
