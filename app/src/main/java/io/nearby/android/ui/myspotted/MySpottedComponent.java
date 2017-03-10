package io.nearby.android.ui.myspotted;

import dagger.Component;
import io.nearby.android.data.source.DataManagerComponent;
import io.nearby.android.NearbyScope;

@NearbyScope
@Component(dependencies = DataManagerComponent.class, modules = MySpottedPresenterModule.class)
public interface MySpottedComponent {
    void inject(MySpottedFragment fragment);
}
