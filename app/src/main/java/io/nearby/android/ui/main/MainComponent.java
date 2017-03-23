package io.nearby.android.ui.main;

import dagger.Component;
import io.nearby.android.NearbyScope;
import io.nearby.android.data.source.DataManagerComponent;

@NearbyScope
@Component(dependencies = DataManagerComponent.class, modules = MainPresenterModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
