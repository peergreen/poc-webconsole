package com.peergreen.webconsole.core.handler.extensions;

import com.peergreen.webconsole.Extension;
import com.peergreen.webconsole.ExtensionPoint;
import com.vaadin.ui.Button;

import javax.annotation.security.RolesAllowed;

/**
 * @author Mohammed Boukada
 */
@Extension
@ExtensionPoint("com.peergreen.webconsole.core.handler.extensions.ExtensionPointProvider.Button")
@RolesAllowed({"admin", "peergreen"})
@TestQualifier(name = "My Awesome Extension")
public class ExtensionExample extends Button {
}
