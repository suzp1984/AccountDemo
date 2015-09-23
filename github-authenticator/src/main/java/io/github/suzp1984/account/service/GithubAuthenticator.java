package io.github.suzp1984.account.service;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import io.github.suzp1984.account.githubdemo.GithubAccount;
import io.github.suzp1984.account.githubdemo.GithubAuthenticatorActivity;

/**
 * Created by moses on 9/15/15.
 */
public class GithubAuthenticator extends AbstractAccountAuthenticator {

    private final String TAG = GithubAuthenticator.class.getSimpleName();
    private final Context mContext;
    private IAuthenticatorServer mAuthenticatorServer;

    public GithubAuthenticator(Context context) {
        super(context);

        mContext = context;
        mAuthenticatorServer = new FakeAuthenticatorServer();
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
            String authTokenType, String[] requiredFeatures, Bundle options)
            throws NetworkErrorException {

        Log.d(TAG, "add Account");

        final Intent intent = new Intent(mContext, GithubAuthenticatorActivity.class);

        intent.putExtra(GithubAuthenticatorActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(GithubAuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(GithubAuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
            Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
            String authTokenType, Bundle options) throws NetworkErrorException {

        Log.d(TAG, "getAuthToken");

        if (!authTokenType.equals(GithubAccount.AUTHTOKEN_TYPE_TEST)) {
            final Bundle bundle = new Bundle();
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return bundle;
        }

        final AccountManager am = AccountManager.get(mContext);
        String authToken = am.peekAuthToken(account, authTokenType);

        if (TextUtils.isEmpty(authToken)) {
            final String password = am.getPassword(account);
            if (password != null) {
                authToken = mAuthenticatorServer.getAuthToken(account.name, password,
                        authTokenType);
            }
        }

        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);

            return result;
        }

        // if authToken is failed, use login activity
        final Intent intent = new Intent(mContext, GithubAuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(GithubAuthenticatorActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(GithubAuthenticatorActivity.ARG_ACCOUNT_NAME, account.name);
        intent.putExtra(GithubAuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
            String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
            String[] features) throws NetworkErrorException {
        return null;
    }
}
