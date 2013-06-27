package com.peergreen.webconsole.core;

import com.peergreen.webconsole.Constants;
import com.peergreen.webconsole.IConsole;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.StaticServiceProperty;

/**
 * Peergreen Administration BaseConsole
 * @author Mohammed Boukada
 */
@Component
@Provides(properties = {@StaticServiceProperty(name = Constants.CONSOLE_NAME, type = "java.lang.String", mandatory = true),
                        @StaticServiceProperty(name = Constants.CONSOLE_ALIAS, type = "java.lang.String", mandatory = true),
                        @StaticServiceProperty(name = Constants.ENABLE_SECURITY, type = "boolean", mandatory = true)})
public class BaseConsole implements IConsole {
}
