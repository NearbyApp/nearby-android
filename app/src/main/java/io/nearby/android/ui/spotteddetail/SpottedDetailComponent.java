package io.nearby.android.ui.spotteddetail;

import dagger.Component;
import io.nearby.android.NearbyScope;
import io.nearby.android.data.source.DataManagerComponent;

@NearbyScope
@Component(dependencies = DataManagerComponent.class,  modules = SpottedDetailPresenterModule.class)
public interface SpottedDetailComponent {
    void inject(SpottedDetailActivity spottedDetailActivity);
}
