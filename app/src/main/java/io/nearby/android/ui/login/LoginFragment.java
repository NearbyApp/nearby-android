package io.nearby.android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import io.nearby.android.R;
import io.nearby.android.google.GoogleApiClientBuilder;
import timber.log.Timber;

/**
 * Created by Marc on 2017-01-20.
 */

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int RC_GOOGLE_LOGIN = 9001;

    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private LoginListener mLoginListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Google Api initialization
        mGoogleApiClient = GoogleApiClientBuilder.build(this.getActivity(),null);

        //Facebook initialization
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mLoginListener.onFacebookLogin(loginResult);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        view.findViewById(R.id.facebook_login_button).setOnClickListener(this);
        view.findViewById(R.id.google_login_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_GOOGLE_LOGIN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                mLoginListener.onGoogleLogin(account);
            }
        }
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("Connection failed - " + connectionResult);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.facebook_login_button:
                facebookSignIn();
                break;
            case R.id.google_login_button:
                googleSignIn();
                break;
        }
    }

    public void setLoginListener(LoginListener listener){
        mLoginListener = listener;
    }

    private void googleSignIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
    }

    private void facebookSignIn(){
        LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this, Arrays.asList("public_profile","email"));
    }

    public interface LoginListener{
        void onGoogleLogin(GoogleSignInAccount account);
        void onFacebookLogin(LoginResult loginResult);
    }
}
