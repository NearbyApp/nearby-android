package io.nearby.android.injection.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.nearby.android.data.local.SharedPreferencesHelper;
import io.nearby.android.data.remote.NearbyService;

/**
 * Created by Marc on 2017-02-09.
 */
@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application){
        mApplication = application;
    }

    @Provides
    Application provideApplication(){
        return mApplication;
    }

    @Provides
    @Singleton
    NearbyService provideNearbyService(){
        NearbyService.Builder builder= new NearbyService.Builder();
        builder.addSharedPreferencesHelper(provideSharedPreferencesHelper());
        return builder.build();
    }

    @Provides
    @Singleton
    SharedPreferencesHelper provideSharedPreferencesHelper(){
        return new SharedPreferencesHelper(mApplication);
    }
}
