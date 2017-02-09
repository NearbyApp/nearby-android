package io.nearby.android.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import io.nearby.android.data.local.SharedPreferencesHelper;
import io.nearby.android.data.remote.NearbyService;
import io.nearby.android.injection.module.ApplicationModule;
import io.nearby.android.ui.BaseActivity;
import io.nearby.android.ui.BaseFragment;
import io.nearby.android.ui.LauncherActivity;

/**
 * Created by Marc on 2017-02-09.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(LauncherActivity baseActivity);
    void inject(BaseFragment baseFragment);
}
