package io.nearby.android.ui.launcher;

import dagger.Component;
import io.nearby.android.NearbyScope;
import io.nearby.android.data.source.DataManagerComponent;

@NearbyScope
@Component(dependencies = DataManagerComponent.class, modules = LauncherPresenterModule.class)
public interface LauncherComponent {

    void inject(LauncherActivity launcherActivity);
}
