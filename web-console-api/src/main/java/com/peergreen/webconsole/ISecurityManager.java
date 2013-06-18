package com.peergreen.webconsole;

/**
 * @author Mohammed Boukada
 */
public interface ISecurityManager {

    boolean isUserInRole(String role);

    boolean isUserInRoles(String[] roles);

    String getUserName();

    boolean isUserLogged();
}
