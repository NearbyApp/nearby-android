package io.nearby.android.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.List;

import io.nearby.android.data.Spotted;

/**
 * Created by Marc on 2017-02-16.
 */

public interface SpottedDataSource {

    interface UserLoginStatusCallback{
        void userIsLoggedIn();
        void userIsNotLoggedIn();
    }

    interface LoginCallback{
        void onAccountCreated();
        void onLoginSuccess();
        void onError();
    }

    interface SpottedCreatedCallback{
        void onSpottedCreated();
        void onError();
    }

    interface MySpottedLoadedCallback{
        void onMySpottedLoaded(List<Spotted> mySpotted);
        void onError();
    }

    interface SpottedDetailsLoadedCallback {
        void onSpottedDetailsLoaded(Spotted spotted);
        void onError();
    }

    interface SpottedLoadedCallback {
        void onSpottedLoaded(List<Spotted> spotted);
        void onError();
    }

    void isUserLoggedIn(UserLoginStatusCallback callback);

    void facebookLogin(String userId, String token, LoginCallback callback);

    void googleLogin(String userId, String token, LoginCallback callback);

    void createSpotted(@NonNull Spotted spotted, @Nullable File image , SpottedCreatedCallback callback);

    void loadMySpotted(MySpottedLoadedCallback callback);

    void loadMyOlderSpotted(int spottedCount, MySpottedLoadedCallback callback);

    void loadSpotted(double minLat,double maxLat,double minLng, double maxLng, boolean locationOnly, SpottedLoadedCallback callback);

    void loadSpottedDetails(String spottedId, SpottedDetailsLoadedCallback callback);
}
