package io.nearby.android.ui.spotteddetail;

import dagger.Module;
import dagger.Provides;

@Module
public class SpottedDetailPresenterModule {
    private SpottedDetailContract.View mView;

    public SpottedDetailPresenterModule(SpottedDetailContract.View view){
        mView = view;
    }

    @Provides
    SpottedDetailContract.View provideNewSpottedContractView(){
        return mView;
    }
}
