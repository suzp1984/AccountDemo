package test.zpcat.org.accountdemo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.zpcat.org.accountdemo.dialog.AccountPickDialogFragment;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.add_account)
    Button mAddAccountBtn;

    @Bind(R.id.get_token)
    Button mGetAuthTokenBtn;

    @Bind(R.id.invalidate_token)
    Button mInvalidateBtn;

    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    AccountManager mAccountManager;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAccountManager = AccountManager.get(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);

        super.onDestroy();
    }

    @OnClick(R.id.add_account)
    public void addAccount() {
        mAccountManager.addAccount(GithubAccount.ACCOUNT_TYPE,
                GithubAccount.AUTHTOKEN_TYPE_TEST,
                null, null, this, new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle bundle = future.getResult();
                            Log.d(TAG, "Add New Account bundle is: " + bundle);
                            Snackbar.make(mFab, bundle.toString(), Snackbar.LENGTH_LONG)
                                    .show();
                            // TODO:dev add account finished do something

                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
    }

    @OnClick(R.id.get_token)
    public void getAuthToken() {
        AccountPickDialogFragment pickDialogFragment = new AccountPickDialogFragment();
        pickDialogFragment.setAccountPickListener(
                new AccountPickDialogFragment.AccountPickListener() {
                    @Override
                    public void onAccountChoice(Account account) {
                        Log.e(TAG, "on Account pick: " + account.name);
                        mAccountManager.getAuthToken(account, GithubAccount.AUTHTOKEN_TYPE_TEST,
                                null,
                                MainActivity.this, new AccountManagerCallback<Bundle>() {
                                    @Override
                                    public void run(AccountManagerFuture<Bundle> future) {
                                        try {
                                            Bundle bundle = future.getResult();

                                            final String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                                            Log.e(TAG, "get AuthToken: " + authToken);
                                            Snackbar.make(mFab, bundle.toString(), Snackbar.LENGTH_LONG)
                                                    .show();
                                        } catch (OperationCanceledException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (AuthenticatorException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, null);
                    }
                });

        pickDialogFragment.show(getSupportFragmentManager(), "account-pick");
    }

    @OnClick(R.id.invalidate_token)
    public void invalidate_token() {

        AccountPickDialogFragment pickDialogFragment = new AccountPickDialogFragment();

        pickDialogFragment.setAccountPickListener(
                new AccountPickDialogFragment.AccountPickListener() {
                    @Override
                    public void onAccountChoice(Account account) {
                        Log.e(TAG, "on Account pick: " + account.name);
                        mAccountManager.getAuthToken(account, GithubAccount.AUTHTOKEN_TYPE_TEST,
                                null,
                                MainActivity.this, new AccountManagerCallback<Bundle>() {
                                    @Override
                                    public void run(AccountManagerFuture<Bundle> future) {
                                        try {
                                            Bundle bundle = future.getResult();

                                            final String authToken = bundle
                                                    .getString(AccountManager.KEY_AUTHTOKEN);
                                            Log.e(TAG, "get AuthToken: " + authToken);
                                            mAccountManager.invalidateAuthToken(
                                                    GithubAccount.ACCOUNT_TYPE, authToken);
                                            Snackbar.make(mFab, "Auth token: (" + authToken
                                                            + ") being invalidated and removed from cache",
                                                    Snackbar.LENGTH_LONG)
                                                    .show();
                                        } catch (OperationCanceledException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (AuthenticatorException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, null);
                    }
                });

        pickDialogFragment.show(getSupportFragmentManager(), "invalidate_token");
    }
}
