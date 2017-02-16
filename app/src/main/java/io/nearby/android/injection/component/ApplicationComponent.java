package io.nearby.android.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import io.nearby.android.ApplicationModule;
import io.nearby.android.ui.launcher.LauncherActivity;
import io.nearby.android.ui.login.LoginActivity;
import io.nearby.android.ui.map.MapFragment;
import io.nearby.android.ui.myspotted.MySpottedFragment;
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

    void inject(MySpottedFragment mySpottedFragment);
    void inject(MapFragment mapFragment);
}
