package io.nearby.android.google;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import io.nearby.android.R;

/**
 * Created by Marc on 2017-01-22.
 */

public class GoogleApiClientBuilder {

    private GoogleApiClientBuilder(){}

    public static GoogleApiClient build(FragmentActivity activity, GoogleApiClient.OnConnectionFailedListener listener){
        //TODO Add server client id.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.client_server_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity , listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return googleApiClient;
    }


}
