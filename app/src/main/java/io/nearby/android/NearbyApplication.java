package io.nearby.android;

import android.app.Application;
import android.util.Log;

import io.nearby.android.injection.component.ApplicationComponent;
import io.nearby.android.injection.component.DaggerApplicationComponent;
import io.nearby.android.injection.module.ApplicationModule;
import timber.log.Timber;

/**
 * Created by Marc on 2017-01-20.
 */

public class NearbyApplication extends Application {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new DebugTree());
        }

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        //Facebook is automatically done when the manifest contains
        // the facebook app-id in a meta-data tag.
    }



    private class DebugTree extends Timber.Tree{

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            switch(priority){
                case Log.ERROR:
                    Log.e(tag,message,t);
                case Log.WARN:
                case Log.DEBUG:
                case Log.INFO:
                case Log.VERBOSE:
                    Log.println(priority,tag,message);
            }
        }
    }
}
