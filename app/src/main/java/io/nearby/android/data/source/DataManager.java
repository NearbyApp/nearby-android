package io.nearby.android.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.local.SpottedLocalDataSource;
import io.nearby.android.data.source.remote.SpottedRemoteDataSource;

@Singleton
public class DataManager implements SpottedDataSource{

    private final SpottedLocalDataSource mLocalDataSource;
    private final SpottedRemoteDataSource mRemoteDataSource;

    @Inject
    public DataManager(SpottedRemoteDataSource remoteDataSource,
                       SpottedLocalDataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    @Override
    public void isUserLoggedIn(UserLoginStatusCallback callback) {
        mLocalDataSource.isUserLoggedIn(callback);
    }

    @Override
    public void facebookLogin(String userId, String token, final LoginCallback callback) {
        mLocalDataSource.setFacebookAuthPrefs(userId, token);

        mRemoteDataSource.facebookLogin(userId, token, new LoginCallback() {
            @Override
            public void onAccountCreated() {
                callback.onAccountCreated();
            }

            @Override
            public void onLoginSuccess() {
                callback.onLoginSuccess();
            }

            @Override
            public void onError(ErrorType errorType) {
                mLocalDataSource.clearFacebookLoginPrefenrences();
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void googleLogin(String userId, String token, final LoginCallback callback) {
        mLocalDataSource.setGoogleAuthPrefs(userId, token);

        mRemoteDataSource.googleLogin(userId, token, new LoginCallback() {
            @Override
            public void onAccountCreated() {
                callback.onAccountCreated();
            }

            @Override
            public void onLoginSuccess() {
                callback.onLoginSuccess();
            }

            @Override
            public void onError(ErrorType errorType) {
                mLocalDataSource.clearGoogleLoginPrefenrences();
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void createSpotted(@NonNull Spotted spotted, @Nullable File image, SpottedCreatedCallback callback) {
        mRemoteDataSource.createSpotted(spotted, image,callback);
    }

    @Override
    public void loadMySpotted(MySpottedLoadedCallback callback) {
        mRemoteDataSource.loadMySpotted(callback);
    }

    @Override
    public void loadSpotted(double minLat,double maxLat,
                            double minLng, double maxLng,
                            boolean locationOnly,
                            SpottedLoadedCallback callback) {
        mRemoteDataSource.loadSpotted(minLat, maxLat,
                minLng, maxLng,
                locationOnly, callback);
    }

    @Override
    public void loadSpottedDetails(String spottedId, SpottedDetailsLoadedCallback callback) {
        mRemoteDataSource.loadSpottedDetails(spottedId, callback);
    }

    @Override
    public void loadMyOlderSpotted(int spottedCount, MySpottedLoadedCallback callback) {
        mRemoteDataSource.loadMyOlderSpotted(spottedCount, callback);
    }

    @Override
    public void getMyNewerSpotteds(Date myOlderSpotted, MySpottedLoadedCallback callback) {
        mRemoteDataSource.getMyNewerSpotteds(myOlderSpotted, callback);
    }

    @Override
    public boolean getDefaultAnonymity() {
        return mLocalDataSource.getDefaultAnonymity();
    }

    @Override
    public void setDefaultAnonymity(boolean anonymity) {
        mLocalDataSource.setDefaultAnonymity(anonymity);
    }

    @Override
    public void getUserInfo(UserInfoLoadedCallback callback) {
        mRemoteDataSource.getUserInfo(callback);
    }

    @Override
    public void linkFacebookAccount(String userId, String token, FacebookLinkAccountCallback callback) {
        mRemoteDataSource.linkFacebookAccount(userId, token, callback);
    }

    @Override
    public void linkGoogleAccount(String userId, String token, GoogleLinkAccountCallback callback) {
        mRemoteDataSource.linkGoogleAccount(userId, token, callback);
    }

    @Override
    public void mergeFacebookAccount(String userId, String token, Callback callback) {
        mRemoteDataSource.mergeFacebookAccount(userId, token, callback);
    }

    @Override
    public void mergeGoogleAccount(String userId, String token, Callback callback) {
        mRemoteDataSource.mergeGoogleAccount(userId, token, callback);
    }

    @Override
    public void signOut(Callback callback) {
        mLocalDataSource.signOut(callback);
    }

    @Override
    public void deactivateAccount(final Callback callback) {
        mRemoteDataSource.deactivateAccount(new Callback() {
            @Override
            public void onSuccess() {
                mLocalDataSource.deactivateAccount(callback);
            }

            @Override
            public void onError(ErrorType errorType) {
                callback.onError(errorType);
            }
        });
    }
}
