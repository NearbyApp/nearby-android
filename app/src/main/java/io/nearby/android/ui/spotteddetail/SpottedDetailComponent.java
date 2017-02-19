package io.nearby.android.ui.spotteddetail;

import dagger.Component;
import io.nearby.android.data.source.DataManagerComponent;
import io.nearby.android.util.NearbyScope;

@NearbyScope
@Component(dependencies = DataManagerComponent.class,  modules = SpottedDetailPresenterModule.class)
public interface SpottedDetailComponent {
    void inject(SpottedDetailActivity spottedDetailActivity);
}
