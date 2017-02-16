package io.nearby.android.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import javax.inject.Inject;

import io.nearby.android.data.source.local.SharedPreferencesHelper;
import io.nearby.android.google.GoogleApiClientBuilder;
import io.nearby.android.ui.MainActivity;
import io.nearby.android.ui.login.LoginActivity;

/**
 * Created by Marc on 2017-01-22.
 */

public class LauncherActivity extends AppCompatActivity {

    @Inject
    SharedPreferencesHelper mSharedPrefHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mSharedPrefHelper.hasUserAlreadySignedIn()){
            int method = mSharedPrefHelper.getLastSignInMethod();

            switch (method){
                case SharedPreferencesHelper.LAST_SIGN_IN_METHOD_FACEBOOK:
                    facebookAuthentification();
                    break;
                case SharedPreferencesHelper.LAST_SIGN_IN_METHOD_GOOGLE:
                    googleAuthentification();
                    break;
            }
        }
        else {
            userIsNotSignedIn();
        }
    }

    /**
     * If an AccessToken exists and is not expired, the user is considered logged in.
     * The finish() method call will be called if the user id logged in.
     *
     * From Facebook developpers site :
     * Native mobile apps using Facebook's SDKs will get long-lived access tokens,
     * good for about 60 days. These tokens will be refreshed once per day when the person
     * using your app makes a request to Facebook's servers. If no requests are made,
     * the token will expire after about 60 days and the person will have to go through the
     * login flow again to get a new token.
     *
     * https://developers.facebook.com/docs/facebook-login/access-tokens/expiration-and-extension
     * Visited 22-01-2017
     *
     * @return true if the user is logged in via Facebook
     */
    private void facebookAuthentification(){
        if(AccessToken.getCurrentAccessToken() != null){
            if(!AccessToken.getCurrentAccessToken().isExpired()) {
                AccessToken.refreshCurrentAccessTokenAsync();
                mSharedPrefHelper.setLastSignInMethod(SharedPreferencesHelper.LAST_SIGN_IN_METHOD_FACEBOOK);
                userIsSignedIn();
                return;
            }
        }

        // If the process gets here, it means that the user was not logged in with facebook
        // or that the token used to log in was expired.
        userIsNotSignedIn();
    }

    /**
     * To validate that the google account exist. We try a silent sign_in.
     * If it fails, the client is not logded in.
     */
    private void googleAuthentification(){
        GoogleApiClient googleApiClient = GoogleApiClientBuilder.build(this,null);

        OptionalPendingResult<GoogleSignInResult> resultOptionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(resultOptionalPendingResult.isDone()){
            handleGoogleResult(resultOptionalPendingResult.get());
        }
        else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            resultOptionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleGoogleResult(googleSignInResult);
                }
            });
        }
    }

    private void handleGoogleResult(GoogleSignInResult googleSignInResult){
        if(googleSignInResult != null && googleSignInResult.isSuccess()){
            mSharedPrefHelper.setLastSignInMethod(SharedPreferencesHelper.LAST_SIGN_IN_METHOD_GOOGLE);
            userIsSignedIn();
        }
        else {
            userIsNotSignedIn();
        }
    }

    private void userIsSignedIn(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void userIsNotSignedIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
