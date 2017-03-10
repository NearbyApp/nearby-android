package io.nearby.android.ui.newspotted;

import dagger.Component;
import io.nearby.android.data.source.DataManagerComponent;
import io.nearby.android.NearbyScope;

@NearbyScope
@Component(dependencies = DataManagerComponent.class, modules = NewSpottedPresenterModule.class)
public interface NewSpottedComponent {

    void inject(NewSpottedActivity newSpottedActivity);

}
