package io.nearby.android;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.nearby.android.data.source.local.SharedPreferencesHelper;
import io.nearby.android.data.source.remote.NearbyService;
import io.nearby.android.data.source.remote.ServiceCreator;

/**
 * Created by Marc on 2017-02-09.
 */
@Module
public class ApplicationModule {

    private final Context mContext;

    public ApplicationModule(Context context){
        mContext = context;
    }

    @Provides
    Context provideApplication(){
        return mContext;
    }

    //@Provides
    //@Singleton
    //NearbyService provideNearbyService(){
    //    ServiceCreator<NearbyService> creator = new ServiceCreator<>(NearbyService.class,NearbyService.ENDPOINT,mApplication,provideSharedPreferencesHelper());
    //    return creator.create();
//
    //    /**NearbyService.Builder builder= new NearbyService.Builder(mApplication);
    //    builder.addSharedPreferencesHelper(provideSharedPreferencesHelper());
    //    return builder.build();*/
    //}
//
    //@Provides
    //@Singleton
    //SharedPreferencesHelper provideSharedPreferencesHelper(){
    //    return new SharedPreferencesHelper(mApplication);
    //}

}
