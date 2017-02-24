package io.nearby.android.ui.newspotted;

import dagger.Module;
import dagger.Provides;

@Module
public class NewSpottedPresenterModule {

    private final NewSpottedContract.View mView;

    public NewSpottedPresenterModule(NewSpottedContract.View view){
        mView = view;
    }

    @Provides
    NewSpottedContract.View provideNewSpottedContractView(){
        return mView;
    }

}
