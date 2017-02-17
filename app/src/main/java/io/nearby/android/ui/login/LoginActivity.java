package io.nearby.android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.google.GoogleApiClientBuilder;
import io.nearby.android.ui.MainActivity;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginContract.View, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_GOOGLE_LOGIN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    @Inject LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        findViewById(R.id.facebook_login_button).setOnClickListener(this);
        findViewById(R.id.google_login_button).setOnClickListener(this);

        initializeGoogle();
        initializeFacebook();

        DaggerLoginComponent.builder()
                .loginPresenterModule(new LoginPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);
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
        mGoogleApiClient = new GoogleApiClientBuilder(this)
                .addSignInApi()
                .enableAutoManage(this,this)
                .build();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = (LoginPresenter) presenter;
    }
}
