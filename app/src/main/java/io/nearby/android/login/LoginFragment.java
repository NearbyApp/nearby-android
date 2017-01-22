package io.nearby.android.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nearby.android.R;

/**
 * Created by Marc on 2017-01-20.
 */

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final int RC_GOOGLE_LOGIN = 101;

    @BindView(R.id.facebook_login_button) Button uFacebookLoginButton;
    @BindView(R.id.google_login_button) Button uGoogleLoginButton;

    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private LoginListener mLoginListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeGoogleApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this,view);

        initializeFacebook();

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
    public void onDestroy() {
        super.onDestroy();

        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //TODO DO something
        Log.e(TAG, "Connection failed");
    }

    @OnClick(R.id.google_login_button)
    public void onGoogleLoginButtonClick(View v){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
    }

    @OnClick(R.id.facebook_login_button)
    public void onFacebookLoginButtonClick(View v){
        LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this, Arrays.asList("public_profile","email"));
    }

    public void setLoginListener(LoginListener listener){
        mLoginListener = listener;
    }

    /**
     * Creates the callback manager which will receive the anwers from the facebook service.
     */
    private void initializeFacebook(){
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mLoginListener.onFacebookLogin(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"Facebook Login result canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,"Facebook Login result error");
            }
        });
    }

    /**
     * Creates the google api objects with the specified GoogleSignInOptions object.
     */
    private void initializeGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .enableAutoManage(this.getActivity() , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public interface LoginListener{
        void onGoogleLogin(GoogleSignInAccount account);
        void onFacebookLogin(LoginResult loginResult);
    }
}
