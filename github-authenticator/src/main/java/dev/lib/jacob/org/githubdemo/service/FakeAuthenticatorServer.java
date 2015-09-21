package dev.lib.jacob.org.githubdemo.service;

/**
 * Created by moses on 9/21/15.
 */
public class FakeAuthenticatorServer implements IAuthenticatorServer {

    @Override
    public String getAuthToken(String user, String pass, String authType) {
        return user + ":" + pass + ":" + authType;
    }
}
