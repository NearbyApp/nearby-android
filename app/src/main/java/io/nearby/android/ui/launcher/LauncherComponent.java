package io.nearby.android.ui.launcher;

import dagger.Component;
import io.nearby.android.data.source.DataManagerComponent;
import io.nearby.android.NearbyScope;

@NearbyScope
@Component(dependencies = DataManagerComponent.class, modules = LauncherPresenterModule.class)
public interface LauncherComponent {

    void inject(LauncherActivity launcherActivity);
}
