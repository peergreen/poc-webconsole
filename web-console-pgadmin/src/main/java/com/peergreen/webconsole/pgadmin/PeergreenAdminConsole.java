package com.peergreen.webconsole.pgadmin;

import com.peergreen.webconsole.IConsole;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 22/05/13
 * Time: 11:00
 * To change this template use File | Settings | File Templates.
 */
@Component
@Instantiate
@Provides
public class PeergreenAdminConsole implements IConsole {

    private final static String CONSOLE_NAME = "Peergreen Administration Console";

    private final static String CONSOLE_ALIAS = "/pgadmin";

    @Override
    public String getConsoleName() {
        return CONSOLE_NAME;
    }

    @Override
    public String getConsoleAlias() {
        return CONSOLE_ALIAS;
    }
}
