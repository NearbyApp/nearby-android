package io.nearby.android.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.nearby.android.data.Spotted;

/**
 * Created by Marc on 2017-02-16.
 */

public interface SpottedDataSource {

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

    void facebookLogin(String userId, String token, LoginCallback callback);

    void googleLogin(String userId, String token, LoginCallback callback);

    void createSpotted(@NonNull Spotted spotted, SpottedCreatedCallback callback);

    void loadMySpotted(MySpottedLoadedCallback callback);

    void loadSpotted(double lat, double lng, boolean locationOnly, SpottedLoadedCallback callback);

    void loadSpottedDetails(Spotted spotted, SpottedDetailsLoadedCallback callback);
}
