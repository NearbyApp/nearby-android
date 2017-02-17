package io.nearby.android.ui.launcher;

import javax.inject.Inject;

import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;

/**
 * Created by Marc on 2017-02-16.
 */

public class LauncherPresenter implements LauncherContract.Presenter {

    private LauncherContract.View mView;
    private DataManager mDataManager;

    @Inject
    public LauncherPresenter(LauncherContract.View view, DataManager mDataManager) {
        this.mView = view;
        this.mDataManager = mDataManager;
    }

    @Inject
    void setupListeners(){
        mView.setPresenter(this);
    }

    @Override
    public void isUserLoggedIn() {
        mDataManager.isUserLoggedIn(new SpottedDataSource.UserLoginStatusCallback() {
            @Override
            public void userIsLoggedIn() {
                mView.onUserLoggedIn();
            }

            @Override
            public void userIsNotLoggedIn() {
                mView.onUserNotLoggedIn();
            }
        });
    }
}
