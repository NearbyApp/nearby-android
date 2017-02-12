package io.nearby.android.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import io.nearby.android.injection.module.ApplicationModule;
import io.nearby.android.ui.LauncherActivity;
import io.nearby.android.ui.login.LoginActivity;
import io.nearby.android.ui.newspotted.NewSpottedActivity;

/**
 * Created by Marc on 2017-02-09.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(LauncherActivity launcherActivity);
    void inject(LoginActivity loginActivity);
    void inject(NewSpottedActivity newSpottedActivity);
}
