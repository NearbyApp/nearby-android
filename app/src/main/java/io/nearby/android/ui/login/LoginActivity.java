package io.nearby.android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

import javax.inject.Inject;

import io.nearby.android.R;
import io.nearby.android.data.source.local.SharedPreferencesHelper;
import io.nearby.android.data.source.remote.NearbyService;
import io.nearby.android.google.GoogleApiClientBuilder;
import io.nearby.android.ui.base.BaseActivity;
import io.nearby.android.ui.MainActivity;
import timber.log.Timber;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginView, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_GOOGLE_LOGIN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private LoginPresenter mPresenter;
    private CallbackManager mCallbackManager;

    @Inject
    NearbyService nearbyService;

    @Inject
    SharedPreferencesHelper mSharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mComponent.inject(this);

        mPresenter = new LoginPresenter(this, nearbyService, mSharedPreferencesHelper);

        findViewById(R.id.facebook_login_button).setOnClickListener(this);
        findViewById(R.id.google_login_button).setOnClickListener(this);

        initializeGoogle();
        initializeFacebook();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_GOOGLE_LOGIN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                mPresenter.loginWithGoogle(account);
            }
        }
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.facebook_login_button:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
            case R.id.google_login_button:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
                break;
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d(connectionResult.getErrorMessage());
    }

    @Override
    public void onLoginSuccessful() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //We remove this activity from the backstack
        finish();
    }

    private void initializeFacebook(){
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mPresenter.loginWithFacebook(loginResult);
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
    }

    private void initializeGoogle(){
        mGoogleApiClient = GoogleApiClientBuilder.build(this, this);
    }
}
