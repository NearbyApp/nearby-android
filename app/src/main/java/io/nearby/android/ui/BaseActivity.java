package io.nearby.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.nearby.android.injection.component.ApplicationComponent;
import io.nearby.android.injection.component.DaggerApplicationComponent;
import io.nearby.android.injection.module.ApplicationModule;

/**
 * Created by Marc on 2017-02-09.
 */

public class BaseActivity extends AppCompatActivity {

    protected ApplicationComponent mComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplication()))
                .build();
    }
}
