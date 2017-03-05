package io.nearby.android.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.Date;
import java.util.List;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.User;

public interface SpottedDataSource {

    interface Callback {
        void onSuccess();
        void onError();
    }

    interface UserLoginStatusCallback{
        void userIsLoggedIn();
        void userIsNotLoggedIn();
        void onError();
    }

    interface SpottedCreatedCallback{
        void onSpottedCreated();
        void onError();
    }

    interface MySpottedLoadedCallback {
        void onMySpottedLoaded(List<Spotted> mySpotted);
        void onError();
    }

    interface UserInfoLoadedCallback{
        void onUserInfoLoaded(User user);
        void onError();
    }

    interface FacebookLinkAccountCallback extends Callback {
        void onFacebookAccountAlreadyExist(String userId, String token);
    }

    interface GoogleLinkAccountCallback extends Callback {
        void onGoogleAccountAlreadyExist(String userId, String token);
    }

    interface SpottedDetailsLoadedCallback{
        void onSpottedDetailsLoaded(Spotted spotted);
        void onError();
    }

    interface SpottedLoadedCallback {
        void onSpottedLoaded(List<Spotted> spotted);
        void onError();
    }

    interface LoginCallback {
        void onAccountCreated();
        void onLoginSuccess();

        void onError();
    }

    void isUserLoggedIn(UserLoginStatusCallback callback);
    void facebookLogin(String userId, String token, LoginCallback callback);

    void googleLogin(String userId, String token, LoginCallback callback);


    void createSpotted(@NonNull Spotted spotted, @Nullable File image , SpottedCreatedCallback callback);

    void loadMySpotted(MySpottedLoadedCallback callback);

    void loadMyOlderSpotted(int spottedCount, MySpottedLoadedCallback callback);

    void getMyNewerSpotteds(Date myOlderSpotted, MySpottedLoadedCallback callback);

    void loadSpotted(double minLat,double maxLat,double minLng, double maxLng, boolean locationOnly, SpottedLoadedCallback callback);

    void loadSpottedDetails(String spottedId, SpottedDetailsLoadedCallback callback);

    /**
     * Gets the default anonymity or the the last anonymity setting used.
     * The default returned value is true.
     * @return true if the user is anonymous.
     */
    boolean getDefaultAnonymity();

    void setDefaultAnonymity(boolean anonymity);

    void getUserInfo(UserInfoLoadedCallback callback);

    void linkFacebookAccount(String userId, String token, FacebookLinkAccountCallback callback);

    void linkGoogleAccount(String userId, String token, GoogleLinkAccountCallback callback);

    void mergeFacebookAccount(String userId, String token, Callback callback);

    void mergeGoogleAccount(String userId, String token, Callback callback);

    void signOut(Callback callback);

    void deactivateAccount(Callback callback);
}
