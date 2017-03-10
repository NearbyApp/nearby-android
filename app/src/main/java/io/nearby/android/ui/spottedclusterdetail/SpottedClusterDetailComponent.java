package io.nearby.android.ui.spottedclusterdetail;

import dagger.Component;
import io.nearby.android.data.source.DataManagerComponent;
import io.nearby.android.NearbyScope;

@NearbyScope
@Component( dependencies = DataManagerComponent.class, modules = SpottedClusterDetailPresenterModule.class)
public interface SpottedClusterDetailComponent {

    void inject(SpottedClusterDetailActivity spottedListActivity);

}
