package io.nearby.android.data.source;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

import dagger.Provides;
import io.nearby.android.data.source.local.SharedPreferencesHelper;
import io.nearby.android.data.source.local.SpottedLocalDataSource;
import io.nearby.android.data.source.remote.NearbyService;
import io.nearby.android.data.source.remote.ServiceCreator;
import io.nearby.android.data.source.remote.SpottedRemoteDataSource;
import io.nearby.android.google.GoogleApiClientBuilder;

/**
 * Created by Marc on 2017-02-16.
 */
@Module
abstract class DataManagerModule {

    @Singleton
    @Binds
    @Local
    abstract SpottedDataSource provideSpottedLocalDataSource(SpottedLocalDataSource spottedLocalDataSource);

    @Singleton
    @Binds
    @Remote
    abstract SpottedDataSource provideSpottedRemoteDataSource(SpottedRemoteDataSource spottedRemoteDataSource);

    @Singleton
    @Binds
    @Local
    abstract SharedPreferencesHelper provideSharedPreferencesHelper(SharedPreferencesHelper sharedPreferencesHelper);

    @Singleton
    @Provides
    static GoogleApiClient provideGoogleApiClient(Context context){
        return new GoogleApiClientBuilder(context)
                .addSignInApi()
                .build();
    }

    @Singleton
    @Provides
    static NearbyService provideNearbyService(SharedPreferencesHelper sharedPreferencesHelper){
        ServiceCreator<NearbyService> creator = new ServiceCreator<>(NearbyService.class,
                NearbyService.ENDPOINT,
                sharedPreferencesHelper);
        return creator.create();
    }
}
