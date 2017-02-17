package io.nearby.android.data.source.local;

import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.Local;
import io.nearby.android.data.source.SpottedDataSource;

/**
 * Created by Marc on 2017-02-16.
 */
@Singleton
@Local
public class SpottedLocalDataSource implements SpottedDataSource {

    private final GoogleApiClient mGoogleApiClient;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Inject
    public SpottedLocalDataSource(SharedPreferencesHelper sharedPreferencesHelper,
                                  GoogleApiClient googleApiClient) {
        mSharedPreferencesHelper = sharedPreferencesHelper;
        mGoogleApiClient = googleApiClient;
    }

    @Override
    public void isUserLoggedIn(UserLoginStatusCallback callback) {
        if(mSharedPreferencesHelper.hasUserAlreadySignedIn()) {
            int method = mSharedPreferencesHelper.getLastSignInMethod();

            switch (method) {
                case SharedPreferencesHelper.LAST_SIGN_IN_METHOD_FACEBOOK:
                    facebookAuthentification(callback);
                    break;
                case SharedPreferencesHelper.LAST_SIGN_IN_METHOD_GOOGLE:
                    googleAuthentification(callback);
                    break;
            }
        }
        else {
            callback.userIsNotLoggedIn();
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
     */
    private void facebookAuthentification(UserLoginStatusCallback callback){
        boolean isUserLoggedIn = false;

        if(AccessToken.getCurrentAccessToken() != null){
            if(!AccessToken.getCurrentAccessToken().isExpired()) {
                AccessToken.refreshCurrentAccessTokenAsync();
                mSharedPreferencesHelper.setLastSignInMethod(SharedPreferencesHelper.LAST_SIGN_IN_METHOD_FACEBOOK);
                isUserLoggedIn = true;
            }
        }

        if(isUserLoggedIn){
            callback.userIsLoggedIn();
        }
        else {
            callback.userIsNotLoggedIn();
        }
    }

    /**
     * To validate that the google account exist. We try a silent sign_in.
     * If it fails, the client is not logded in.
     */
    private void googleAuthentification(final UserLoginStatusCallback callback){
        mGoogleApiClient.connect();
        OptionalPendingResult<GoogleSignInResult> resultOptionalPendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(resultOptionalPendingResult.isDone()){
            handleGoogleResult(resultOptionalPendingResult.get(), callback);
            mGoogleApiClient.disconnect();
        }
        else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            resultOptionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    mGoogleApiClient.disconnect();
                    handleGoogleResult(googleSignInResult, callback);
                }
            });
        }
    }

    private void handleGoogleResult(GoogleSignInResult googleSignInResult, UserLoginStatusCallback callback){
        if(googleSignInResult != null && googleSignInResult.isSuccess()){
            mSharedPreferencesHelper.setLastSignInMethod(SharedPreferencesHelper.LAST_SIGN_IN_METHOD_GOOGLE);
            callback.userIsLoggedIn();
        }
        else {
            callback.userIsNotLoggedIn();
        }
    }

    @Override
    public void facebookLogin(String userId, String token, LoginCallback callback) {
        
    }

    @Override
    public void googleLogin(String userId, String token, LoginCallback callback) {

    }

    @Override
    public void createSpotted(@NonNull Spotted spotted, SpottedCreatedCallback callback) {

    }

    @Override
    public void loadMySpotted(MySpottedLoadedCallback callback) {

    }

    @Override
    public void loadSpotted(double lat, double lng, boolean locationOnly, SpottedLoadedCallback callback) {

    }

    @Override
    public void loadSpottedDetails(Spotted spotted, SpottedDetailsLoadedCallback callback) {

    }
}
