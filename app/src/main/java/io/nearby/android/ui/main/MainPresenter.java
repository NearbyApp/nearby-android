package io.nearby.android.ui.main;

import javax.inject.Inject;

import io.nearby.android.data.User;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.ui.BasePresenter;

public class MainPresenter implements MainContract.Presenter{

    private MainContract.View mView;
    private DataManager mDataManager;

    @Inject
    public MainPresenter(MainContract.View view, DataManager dataManager){
        mView = view;
        mDataManager = dataManager;
    }

    @Inject
    void setupListeners(){
        mView.setPresenter(this);
    }

    @Override
    public void getUserInfo() {
        mDataManager.getUserInfo(new SpottedDataSource.UserInfoLoadedCallback() {
            @Override
            public void onUserInfoLoaded(User user) {
                mView.onUserInfoReceived(user);
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    // Do nothing
                    // Keep both link account preferences disabled
                }
            }
        });
    }

}
