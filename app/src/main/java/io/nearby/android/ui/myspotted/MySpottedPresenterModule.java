package io.nearby.android.ui.myspotted;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Marc on 2017-02-16.
 */
@Module
public class MySpottedPresenterModule {

    private final MySpottedContract.View mView;

    public MySpottedPresenterModule(MySpottedContract.View view) {
        this.mView = view;
    }

    @Provides
    MySpottedContract.View provideMySpottedContractView(){
        return mView;
    }
}
