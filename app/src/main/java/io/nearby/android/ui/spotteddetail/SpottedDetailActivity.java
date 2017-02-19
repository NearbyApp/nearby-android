package io.nearby.android.ui.spotteddetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;

public class SpottedDetailActivity extends AppCompatActivity implements SpottedDetailContract.View{

    @Inject
    SpottedDetailPresenter mPresenter;

    @Override
    public void setPresenter(SpottedDetailContract.Presenter presenter) {
        mPresenter = (SpottedDetailPresenter) presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerSpottedDetailComponent.builder()
                .spottedDetailPresenterModule(new SpottedDetailPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);
    }
}
