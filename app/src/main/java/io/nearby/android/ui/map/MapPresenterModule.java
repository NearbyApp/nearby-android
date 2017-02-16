package io.nearby.android.ui.map;

import dagger.Module;
import dagger.Provides;

@Module
public class MapPresenterModule {

    private final MapContract.View mView;

    public MapPresenterModule(MapContract.View view) {
        this.mView = view;
    }

    @Provides
    MapContract.View provideMapContractView(){
        return mView;
    }
}
