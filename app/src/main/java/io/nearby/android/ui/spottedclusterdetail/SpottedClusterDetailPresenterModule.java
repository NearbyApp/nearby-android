package io.nearby.android.ui.spottedclusterdetail;

import dagger.Module;
import dagger.Provides;

@Module
public class SpottedClusterDetailPresenterModule {
    private SpottedClusterDetailContract.View mView;

    public SpottedClusterDetailPresenterModule(SpottedClusterDetailContract.View view) {
        mView = view;
    }

    @Provides
    SpottedClusterDetailContract.View provideSpottedListContractView(){
        return mView;
    }
}
