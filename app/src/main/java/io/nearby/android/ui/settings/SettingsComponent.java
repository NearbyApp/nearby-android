package io.nearby.android.ui.settings;

import dagger.Component;
import io.nearby.android.data.source.DataManagerComponent;
import io.nearby.android.util.NearbyScope;

@NearbyScope
@Component( dependencies = DataManagerComponent.class, modules = SettingsPresenterModule.class)
public interface SettingsComponent {

    void inject(SettingsActivity settingsActivity);

}
