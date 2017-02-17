package io.nearby.android.ui.launcher;

import dagger.Module;
import dagger.Provides;

@Module
public class LauncherPresenterModule {

    private final LauncherContract.View mView;

    public LauncherPresenterModule(LauncherContract.View view){
        mView = view;
    }

    @Provides
    LauncherContract.View provideLauncherContractView(){
        return mView;
    }
}
