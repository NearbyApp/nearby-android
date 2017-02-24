package io.nearby.android.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

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
import io.nearby.android.ui.launcher.LauncherActivity;
import timber.log.Timber;

public class SettingsFragment extends PreferenceFragment implements SettingsContract.View {

    private final static String PREF_CONNECT_FACEBOOK   = "pref_connect_facebook";
    private final static String PREF_CONNECT_GOOGLE     = "pref_connect_google";
    private final static String PREF_LOGOUT             = "pref_logout";
    private final static String PREF_DEACTIVATE_ACCOUNT = "pref_deactivate_account";

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
                Timber.d("Facebook Login result canceled");
            }

            @Override
            public void onError(FacebookException error) {
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
            case PREF_CONNECT_FACEBOOK:
                //TODO Add dialog
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
            case PREF_CONNECT_GOOGLE:
                //TODO Add dialog
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
                break;
            case PREF_LOGOUT:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.logout)
                        .setMessage(R.string.logout_message)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.logout();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .create().show();
                break;
            case PREF_DEACTIVATE_ACCOUNT:
                //TODO Add dialog
                mPresenter.deactivateAccount();
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
    public void onUserInfoReceived(User user) {
        if(user != null){
            if(!user.hasGoogleAccount()){
                getPreferenceScreen().findPreference(PREF_CONNECT_GOOGLE).setEnabled(true);
            }
            if(!user.hasFacebookAccount()){
                getPreferenceScreen().findPreference(PREF_CONNECT_FACEBOOK).setEnabled(true);
            }
        }
    }

    @Override
    public void onSignOutCompleted() {
        navigateToLauncherActivity();
    }

    @Override
    public void onAccountDeactivated() {
        navigateToLauncherActivity();
    }

    private void navigateToLauncherActivity(){
        Intent intent = new Intent(getActivity(), LauncherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
