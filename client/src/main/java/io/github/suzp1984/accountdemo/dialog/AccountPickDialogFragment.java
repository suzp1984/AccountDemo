package io.github.suzp1984.accountdemo.dialog;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.suzp1984.accountdemo.GithubAccount;

public class AccountPickDialogFragment extends DialogFragment {

    private AccountManager mAccountManager;
    private final List<String> mAccountNames = new ArrayList<>();
    private final List<Account> mAccountList = new ArrayList<>();

    private Context mContext;
    private AccountPickListener mPickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        mAccountManager = AccountManager.get(context);

        Account[] accounts = mAccountManager.getAccountsByType(GithubAccount.ACCOUNT_TYPE);
        for (Account account : accounts) {
            mAccountNames.add(account.name);
            mAccountList.add(account);
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick an Account")
                .setAdapter(new AccountAdapter(mContext,
                                android.R.layout.simple_list_item_1, mAccountNames),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mPickListener != null) {
                                    mPickListener.onAccountChoice(mAccountList.get(which));
                                }
                            }
                        });

        return builder.create();
    }

    public void setAccountPickListener(AccountPickListener listener) {
        mPickListener = listener;
    }

    public class AccountAdapter extends ArrayAdapter<String> {

        public AccountAdapter(Context context, int resource, List<String> accounts) {
            super(context, resource, accounts);
        }
    }

    public interface AccountPickListener {
        void onAccountChoice(Account account);
    }
}
