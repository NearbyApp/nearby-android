package io.nearby.android.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import io.nearby.android.data.local.SharedPreferencesHelper;
import io.nearby.android.data.remote.NearbyService;
import io.nearby.android.injection.module.ApplicationModule;

/**
 * Created by Marc on 2017-02-09.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    NearbyService getNearbyService();
    SharedPreferencesHelper getSharedPreferencesHelper();
}
