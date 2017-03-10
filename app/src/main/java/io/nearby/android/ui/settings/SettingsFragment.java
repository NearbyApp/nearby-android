package io.nearby.android.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

import io.nearby.android.R;
import io.nearby.android.data.User;
import io.nearby.android.google.GoogleApiClientBuilder;
import io.nearby.android.ui.BaseFragment;
import io.nearby.android.ui.launcher.LauncherActivity;
import timber.log.Timber;

public class SettingsFragment extends PreferenceFragment implements SettingsContract.View {

    private final static String PREF_LINK_FACEBOOK_ACCOUNT  = "pref_link_facebook";
    private final static String PREF_LINK_GOOGLE_ACCOUNT    = "pref_link_google";
    private final static String PREF_LOGOUT                 = "pref_logout";
    private final static String PREF_DEACTIVATE_ACCOUNT     = "pref_deactivate_account";

    private static final int RC_GOOGLE_LOGIN = 9001;

    private SettingsContract.Presenter mPresenter;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        getPreferenceScreen().getContext().setTheme(R.style.PreferenceScreenStyle);

        mGoogleApiClient = new GoogleApiClientBuilder(getActivity())
                .addSignInApi()
                .build();

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mPresenter.linkFacebookAccount(loginResult);
            }

            @Override
            public void onCancel() {
                linkAccountFailed();
                Timber.d("Facebook Login result canceled");
            }

            @Override
            public void onError(FacebookException error) {
                linkAccountFailed();
                Timber.d("Facebook Login result error");
            }
        });

        mPresenter.getUserInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        switch (preference.getKey()){
            case PREF_LINK_FACEBOOK_ACCOUNT:
                linkFacebookAccount();
                break;
            case PREF_LINK_GOOGLE_ACCOUNT:
                linkGoogleAccount();
                break;
            case PREF_LOGOUT:
                logout();
                break;
            case PREF_DEACTIVATE_ACCOUNT:
                deactivate();
                break;

        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_GOOGLE_LOGIN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                mPresenter.linkGoogleAccount(account);
            }
            else {
                linkAccountFailed();
            }
        }
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onUserAccountDisabled() {
        BaseFragment.showAccountAlreadyDisabledDialog(getActivity());
    }

    @Override
    public void onUserUnauthorized() {
        Intent intent = new Intent(getActivity(), LauncherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onUserInfoReceived(User user) {
        if(user != null){
            if(!user.hasGoogleAccount()){
                getPreferenceScreen().findPreference(PREF_LINK_GOOGLE_ACCOUNT).setEnabled(true);
            }
            if(!user.hasFacebookAccount()){
                getPreferenceScreen().findPreference(PREF_LINK_FACEBOOK_ACCOUNT).setEnabled(true);
            }
        }
    }

    @Override
    public void onGoogleAccountLinked() {
        getPreferenceScreen().findPreference(PREF_LINK_GOOGLE_ACCOUNT).setEnabled(false);
    }

    @Override
    public void onFacebookAccountLinked() {
        getPreferenceScreen().findPreference(PREF_LINK_FACEBOOK_ACCOUNT).setEnabled(false);
    }

    @Override
    public void onSignOutCompleted() {
        navigateToLauncherActivity();
    }

    @Override
    public void onAccountDeactivated() {
        navigateToLauncherActivity();
    }

    @Override
    public void onGoogleAccountAlreadyExist(final String userId, final String token) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Merge account")
                .setMessage("This Google account already exists. Would you like to merge it with your current Facebook account?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.mergeGoogleAccount(userId, token);
                    }
                })
                .setNegativeButton(R.string.no, null)
                .create().show();
    }

    @Override
    public void onFacebookAccountAlreadyExist(final String userId, final String token) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Merge account")
                .setMessage("This Facebook account already exists. Would you like to merge it with your current Google account?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.mergeFacebookAccount(userId, token);
                    }
                })
                .setNegativeButton(R.string.no, null)
                .create().show();
    }

    @Override
    public void onGoogleAccountMerged() {
        getPreferenceScreen().findPreference(PREF_LINK_GOOGLE_ACCOUNT).setEnabled(false);
    }

    @Override
    public void onFacebookAccountMerged() {
        getPreferenceScreen().findPreference(PREF_LINK_FACEBOOK_ACCOUNT).setEnabled(false);
    }

    @Override
    public void linkAccountFailed() {
        Toast.makeText(getActivity(), R.string.link_account_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void deactivateAccountFailed() {
        Toast.makeText(getActivity(), R.string.account_dactivation_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void mergeAccountFailed() {
        Toast.makeText(getActivity(), R.string.merge_account_failed, Toast.LENGTH_LONG).show();
    }

    private void navigateToLauncherActivity(){
        Intent intent = new Intent(getActivity(), LauncherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void logout(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.logout();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    private void linkFacebookAccount(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.link_facebook_account)
                .setMessage(R.string.link_facebook_account_message)
                .setPositiveButton(R.string.link, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginManager.getInstance().logInWithReadPermissions(SettingsFragment.this, Arrays.asList("public_profile", "email"));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .create().show();
    }

    private void linkGoogleAccount(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.link_google_account)
                .setMessage(R.string.link_google_account_message)
                .setPositiveButton(R.string.link, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    private void deactivate(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.deactivate_account)
                .setMessage(R.string.deactivate_account_message)
                .setPositiveButton(R.string.deactivate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deactivateAccount();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }
}
