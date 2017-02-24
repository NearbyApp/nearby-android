package io.nearby.android.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;

/**
 * Created by Marc on 2017-02-21.
 */

public class SettingsActivity extends AppCompatActivity{

    @Inject SettingsPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment settingsFragment = SettingsFragment.newInstance();

        DaggerSettingsComponent.builder()
                .settingsPresenterModule(new SettingsPresenterModule(settingsFragment))
                .dataManagerComponent(((NearbyApplication) getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, settingsFragment)
                .commit();
    }
}
