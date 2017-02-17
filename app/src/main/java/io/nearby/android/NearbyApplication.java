package io.nearby.android;

import android.app.Application;
import android.util.Log;

import io.nearby.android.data.source.DaggerDataManagerComponent;
import io.nearby.android.data.source.DataManagerComponent;
import timber.log.Timber;

/**
 * Created by Marc on 2017-01-20.
 */

public class NearbyApplication extends Application {

    private DataManagerComponent mDataManagerComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new DebugTree());
        }

        //Facebook is automatically done when the manifest contains
        // the facebook app-id in a meta-data tag.

        mDataManagerComponent = DaggerDataManagerComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
    }

    public DataManagerComponent getDataManagerComponent(){
        return mDataManagerComponent;
    }

    private class DebugTree extends Timber.Tree{

        private final static String TAG = "NearbyApplicationDebug";

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            switch(priority){
                case Log.ERROR:
                    Log.e(TAG,message,t);
                    break;
                case Log.WARN:
                    Log.w(TAG,message);
                    break;
                case Log.DEBUG:
                    Log.d(TAG,message);
                    break;
                case Log.INFO:
                    Log.i(TAG,message);
                    break;
                case Log.VERBOSE:
                    Log.v(TAG,message);
                    break;
            }
        }
    }
}
