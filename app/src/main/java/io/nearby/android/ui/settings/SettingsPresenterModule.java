package io.nearby.android.ui.settings;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsPresenterModule {

    private final SettingsContract.View mView;

    public SettingsPresenterModule(SettingsContract.View view){
        mView = view;
    }

    @Provides
    SettingsContract.View provideSettingsContractView(){
        return mView;
    }
}
