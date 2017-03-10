package io.nearby.android.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import io.nearby.android.R;

/**
 * Created by Marc on 2017-01-22.
 */

public class GoogleApiClientBuilder {

    private Context mContext;
    private GoogleApiClient.Builder mGoogleApiBuilder;

    public GoogleApiClientBuilder(Context context){
        mContext = context;

        mGoogleApiBuilder = new GoogleApiClient.Builder(mContext);
    }

    public GoogleApiClientBuilder addSignInApi(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.client_server_id))
                .requestEmail()
                .build();
        mGoogleApiBuilder.addApi(Auth.GOOGLE_SIGN_IN_API, gso);

        return this;
    }

    public GoogleApiClientBuilder addLocationServicesApi(){
        mGoogleApiBuilder.addApi(LocationServices.API);
        return this;
    }

    public GoogleApiClientBuilder addConnectionCallbacks(GoogleApiClient.ConnectionCallbacks callbacks){
        mGoogleApiBuilder.addConnectionCallbacks(callbacks);
        return this;
    }

    public GoogleApiClientBuilder addOnConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener listener){
        mGoogleApiBuilder.addOnConnectionFailedListener(listener);
        return this;
    }

    public GoogleApiClientBuilder enableAutoManage(FragmentActivity activity,
                                       GoogleApiClient.OnConnectionFailedListener listener){
        mGoogleApiBuilder.enableAutoManage(activity, listener);

        return this;
    }

    public GoogleApiClient build(){
        return mGoogleApiBuilder.build();
    }
}
