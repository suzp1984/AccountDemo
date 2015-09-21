package dev.lib.jacob.org.githubdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GithubAuthenticatorService extends Service {

    public GithubAuthenticatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new GithubAuthenticator(this).getIBinder();
    }
}
