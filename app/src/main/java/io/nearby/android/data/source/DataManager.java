package io.nearby.android.data.source;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.local.SharedPreferencesHelper;
import io.nearby.android.data.source.local.SpottedLocalDataSource;
import io.nearby.android.data.source.remote.SpottedRemoteDataSource;

/**
 * Created by Marc on 2017-02-16.
 */
@Singleton
public class DataManager implements SpottedDataSource{

    private final SpottedLocalDataSource mLocalDataSource;
    private final SpottedRemoteDataSource mRemoteDataSource;
    private final SharedPreferencesHelper mSharedPreferencesHelper;

    @Inject
    public DataManager(SpottedRemoteDataSource remoteDataSource,
                       SpottedLocalDataSource localDataSource,
                       SharedPreferencesHelper sharedPreferencesHelper) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
        mSharedPreferencesHelper = sharedPreferencesHelper;
    }

    @Override
    public void facebookLogin(String userId, String token, LoginCallback callback) {
        mSharedPreferencesHelper.setFacebookUserId(userId);
        mSharedPreferencesHelper.setFacebookToken(token);
        mSharedPreferencesHelper.setLastSignInMethod(SharedPreferencesHelper.LAST_SIGN_IN_METHOD_FACEBOOK);

        mRemoteDataSource.googleLogin(userId, token, callback);
    }

    @Override
    public void googleLogin(String userId, String token, LoginCallback callback) {
        mSharedPreferencesHelper.setGoogleUserId(userId);
        mSharedPreferencesHelper.setGoogleToken(token);
        mSharedPreferencesHelper.setLastSignInMethod(SharedPreferencesHelper.LAST_SIGN_IN_METHOD_GOOGLE);

        mRemoteDataSource.googleLogin(userId, token, callback);
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
