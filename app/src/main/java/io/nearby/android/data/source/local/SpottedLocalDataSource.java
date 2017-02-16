package io.nearby.android.data.source.local;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.Local;
import io.nearby.android.data.source.SpottedDataSource;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Marc on 2017-02-16.
 */
@Singleton
@Local
public class SpottedLocalDataSource implements SpottedDataSource {


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
