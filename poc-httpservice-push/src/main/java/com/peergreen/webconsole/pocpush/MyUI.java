package com.peergreen.webconsole.pocpush;

import com.vaadin.annotations.Push;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Push
public class MyUI extends UI {

    private final VerticalLayout mainLayout;

    public Layout getLayout() {
        return mainLayout;
    }


    public MyUI() {
        this.mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        setContent(mainLayout);
    }

    @Override
    protected void init(VaadinRequest request) {
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        mainLayout.addComponent(new Button("refresh"));

    }
}