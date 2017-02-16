package io.nearby.android;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Marc on 2017-02-09.
 */
@Module
public class ApplicationModule {

    private final Context mContext;

    public ApplicationModule(Context context){
        mContext = context;
    }

    @Provides
    Context provideApplication(){
        return mContext;
    }

}
