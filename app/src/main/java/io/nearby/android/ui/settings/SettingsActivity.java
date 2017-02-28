package io.nearby.android.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;

public class SettingsActivity extends AppCompatActivity{

    @Inject SettingsPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        SettingsFragment settingsFragment = SettingsFragment.newInstance();

        DaggerSettingsComponent.builder()
                .settingsPresenterModule(new SettingsPresenterModule(settingsFragment))
                .dataManagerComponent(((NearbyApplication) getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.content, settingsFragment)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
