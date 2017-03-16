package io.nearby.android.ui.map;

import dagger.Component;
import io.nearby.android.NearbyScope;
import io.nearby.android.data.source.DataManagerComponent;

@NearbyScope
@Component(dependencies = DataManagerComponent.class, modules = MapPresenterModule.class)
public interface MapComponent {

    void inject(MapFragment fragment);


}
