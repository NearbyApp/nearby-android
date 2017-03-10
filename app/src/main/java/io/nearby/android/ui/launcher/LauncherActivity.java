package io.nearby.android.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.ui.MainActivity;
import io.nearby.android.ui.login.LoginActivity;

/**
 * Created by Marc on 2017-01-22.
 */

public class LauncherActivity extends AppCompatActivity implements LauncherContract.View{

    @Inject
    LauncherPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerLauncherComponent.builder()
                .launcherPresenterModule(new LauncherPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getApplication())
                        .getDataManagerComponent())
                .build()
                .inject(this);

        mPresenter.isUserLoggedIn();
    }

    @Override
    public void setPresenter(LauncherContract.Presenter presenter) {
        mPresenter = (LauncherPresenter) presenter;
    }

    @Override
    public void onUserAccountDisabled() {

    }

    @Override
    public void onUserUnauthorized() {

    }

    @Override
    public void onUserNotLoggedIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUserLoggedIn() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
