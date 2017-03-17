package io.nearby.android.ui.main;

import dagger.Component;
import io.nearby.android.NearbyScope;
import io.nearby.android.data.source.DataManagerComponent;
import io.nearby.android.ui.myspotted.MySpottedFragment;

@NearbyScope
@Component(dependencies = DataManagerComponent.class, modules = MainPresenterModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
