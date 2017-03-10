package io.nearby.android.ui.myspotted;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.ui.BasePresenter;

public class MySpottedPresenter implements MySpottedContract.Presenter{

    private MySpottedContract.View mView;
    private DataManager mDataManager;

    @Inject
    public MySpottedPresenter(MySpottedContract.View view, DataManager dataManager){
        mView = view;
        mDataManager = dataManager;
    }

    @Inject
    void setupListeners(){
        mView.setPresenter(this);
    }

    @Override
    public void loadMySpotted(){
        mDataManager.loadMySpotted(new SpottedDataSource.MySpottedLoadedCallback() {
            @Override
            public void onMySpottedLoaded(List<Spotted> mySpotted) {
                mView.hideLoadingProgressBar();
                mView.onMySpottedReceived(mySpotted);
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                mView.hideLoadingProgressBar();
                if(!BasePresenter.manageError(mView, errorType)){
                    mView.loadingMySpottedFailed();
                }
            }
        });
    }

    @Override
    public void refreshMySpotted(Date myOlderSpotted){
        mDataManager.getMyNewerSpotteds(myOlderSpotted, new SpottedDataSource.MySpottedLoadedCallback(){
            @Override
            public void onMySpottedLoaded(List<Spotted> mySpotted) {
                mView.onMyNewerSpottedReceived(mySpotted);
                mView.stopRefreshing();
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                mView.stopRefreshing();
                if(!BasePresenter.manageError(mView, errorType)){
                    mView.refreshFailed();
                }
            }
        });
    }

    @Override
    public void loadMyOlderSpotted(int spottedCount){
        mDataManager.loadMyOlderSpotted(spottedCount, new SpottedDataSource.MySpottedLoadedCallback(){

            @Override
            public void onMySpottedLoaded(List<Spotted> mySpotted) {
                mView.onMyOlderSpottedReceived(mySpotted);
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    mView.loadOlderFailed();
                }
            }
        });
    }
}
