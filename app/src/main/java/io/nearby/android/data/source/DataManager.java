package io.nearby.android.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.User;
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
    public void createSpotted(@NonNull Spotted spotted, @Nullable File image, final SpottedCreatedCallback callback) {
        mRemoteDataSource.createSpotted(spotted, image, new SpottedCreatedCallback() {
            @Override
            public void onSpottedCreated() {
                callback.onSpottedCreated();
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void loadMySpotted(final MySpottedLoadedCallback callback) {
        mRemoteDataSource.loadMySpotted(new MySpottedLoadedCallback() {
            @Override
            public void onMySpottedLoaded(List<Spotted> mySpotted) {
                callback.onMySpottedLoaded(mySpotted);
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void loadSpotted(double minLat, double maxLat,
                            double minLng, double maxLng,
                            boolean locationOnly,
                            final SpottedLoadedCallback callback) {
        // Add small buffer to min and max so that if we have multiple spotteds at the same
        // location, we won't send identical min and max.
        minLat -= 0.000001;
        minLng -= 0.000001;
        maxLat += 0.000001;
        maxLng += 0.000001;

        mRemoteDataSource.loadSpotted(minLat, maxLat,
                minLng, maxLng,
                locationOnly, new SpottedLoadedCallback() {
                    @Override
                    public void onSpottedLoaded(List<Spotted> spotted) {
                        callback.onSpottedLoaded(spotted);
                    }

                    @Override
                    public void onError(ErrorType errorType) {
                        manageError(errorType);
                        callback.onError(errorType);
                    }
                });
    }

    @Override
    public void loadSpottedDetails(String spottedId, final SpottedDetailsLoadedCallback callback) {
        mRemoteDataSource.loadSpottedDetails(spottedId, new SpottedDetailsLoadedCallback() {
            @Override
            public void onSpottedDetailsLoaded(Spotted spotted) {
                callback.onSpottedDetailsLoaded(spotted);
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void loadMyOlderSpotted(int spottedCount, final MySpottedLoadedCallback callback) {
        mRemoteDataSource.loadMyOlderSpotted(spottedCount, new MySpottedLoadedCallback() {
            @Override
            public void onMySpottedLoaded(List<Spotted> mySpotted) {
                callback.onMySpottedLoaded(mySpotted);
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void getMyNewerSpotteds(Date myOlderSpotted, final MySpottedLoadedCallback callback) {
        mRemoteDataSource.getMyNewerSpotteds(myOlderSpotted, new MySpottedLoadedCallback() {
            @Override
            public void onMySpottedLoaded(List<Spotted> mySpotted) {
                callback.onMySpottedLoaded(mySpotted);
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
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
    public void getUserInfo(final UserInfoLoadedCallback callback) {
        mRemoteDataSource.getUserInfo(new UserInfoLoadedCallback() {
            @Override
            public void onUserInfoLoaded(User user) {
                callback.onUserInfoLoaded(user);
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void linkFacebookAccount(String userId, String token, final FacebookLinkAccountCallback callback) {
        mRemoteDataSource.linkFacebookAccount(userId, token, new FacebookLinkAccountCallback() {
            @Override
            public void onFacebookAccountAlreadyExist(String userId, String token) {
                callback.onFacebookAccountAlreadyExist(userId, token);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void linkGoogleAccount(String userId, String token, final GoogleLinkAccountCallback callback) {
        mRemoteDataSource.linkGoogleAccount(userId, token, new GoogleLinkAccountCallback() {
            @Override
            public void onGoogleAccountAlreadyExist(String userId, String token) {
                callback.onGoogleAccountAlreadyExist(userId,token);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void mergeFacebookAccount(String userId, String token, final Callback callback) {
        mRemoteDataSource.mergeFacebookAccount(userId, token, new Callback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void mergeGoogleAccount(String userId, String token, final Callback callback) {
        mRemoteDataSource.mergeGoogleAccount(userId, token, new Callback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
    }

    @Override
    public void signOut(final Callback callback) {
        mLocalDataSource.signOut(new Callback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError(ErrorType errorType) {
                manageError(errorType);
                callback.onError(errorType);
            }
        });
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
                manageError(errorType);
                callback.onError(errorType);
            }
        });
    }

    private void manageError(ErrorType errorType){
        if(errorType == ErrorType.UnauthorizedUser || errorType == ErrorType.DisabledUser){
            mLocalDataSource.forceSignOutOrForceDeactivate();
        }
    }
}
