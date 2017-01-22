package io.nearby.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.concurrent.TimeUnit;

import io.nearby.android.google.GoogleApiClientBuilder;
import io.nearby.android.login.LoginActivity;

/**
 * Created by Marc on 2017-01-22.
 */

public class LauncherActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        boolean isLoggedIn = isUserLogedWithFacebook() || isUserLoggedInWithGoogle();

        if(isLoggedIn){
            Toast.makeText(this,"Logged in",Toast.LENGTH_LONG).show();
            intent = new Intent(this, LoginActivity.class);
        }
        else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }

    /**
     * If an AccessToken exists and is not expired, the user is considered logged in.
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
    private boolean isUserLogedWithFacebook(){
        boolean isLoggedIn = false;

        if(AccessToken.getCurrentAccessToken() != null){
            if(!AccessToken.getCurrentAccessToken().isExpired()) {
                isLoggedIn = true;
            }
        }

        return isLoggedIn;
    }

    /**
     * To validate that the google account exist. We try a silent sign_in.
     * If it fails, the client is not logded in.
     * @return True if the session is still valid.
     */
    private boolean isUserLoggedInWithGoogle(){
        boolean isLoggedIn = false;

        GoogleApiClient googleApiClient = GoogleApiClientBuilder.build(this,null);

        OptionalPendingResult<GoogleSignInResult> resultOptionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(resultOptionalPendingResult.isDone()){
            isLoggedIn = resultOptionalPendingResult.get().isSuccess();
        }

        return isLoggedIn;
    }
}
