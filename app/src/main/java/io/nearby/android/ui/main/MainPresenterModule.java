package io.nearby.android.ui.main;

import dagger.Module;
import dagger.Provides;

@Module
public class MainPresenterModule {

    private final MainContract.View mView;

    public MainPresenterModule(MainContract.View view) {
        this.mView = view;
    }

    @Provides
    MainContract.View provideMainContractView(){
        return mView;
    }
}
