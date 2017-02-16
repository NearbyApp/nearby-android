package io.nearby.android.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.nearby.android.data.source.DaggerDataManagerComponent;
import io.nearby.android.ApplicationModule;

/**
 * Created by Marc on 2017-02-09.
 */

public class BaseActivity extends AppCompatActivity {

    protected DaggerDataManagerComponent mComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mComponent = DaggerDataManagerComponent.builder()
        //        .applicationModule(new ApplicationModule())
        //        .(new DataManagerModule(getApplication()))
        //        .build();
    }
}
