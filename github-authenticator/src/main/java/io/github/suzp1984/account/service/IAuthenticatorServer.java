package io.github.suzp1984.account.service;

/**
 * Created by moses on 9/21/15.
 */
public interface IAuthenticatorServer {
    String getAuthToken(final String user, final String pass, String authType);
}
