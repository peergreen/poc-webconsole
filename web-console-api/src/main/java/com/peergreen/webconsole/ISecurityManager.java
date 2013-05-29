package com.peergreen.webconsole;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mohammed
 * Date: 27/05/13
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
public interface ISecurityManager {

    boolean isUserInRole(String role);

    boolean isUserInRoles(List<String> roles);

    String getUserName();

    boolean isUserLogged();
}
