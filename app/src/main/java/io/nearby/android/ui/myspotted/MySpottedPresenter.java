package io.nearby.android.ui.myspotted;

import java.util.List;

import javax.inject.Inject;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-08.
 */

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
                mView.onMySpottedReceived(mySpotted);
            }

            @Override
            public void onError() {
                //TODO Manage errors
            }
        });
    }

    public void refreshMySpotted(){

    }

    public void loadMyOlderSpotted(int spottedCount){
        mDataManager.loadMyOlderSpotted(spottedCount, new SpottedDataSource.MySpottedLoadedCallback(){

            @Override
            public void onMySpottedLoaded(List<Spotted> mySpotted) {
                mView.onMyOlderSpottedReceived(mySpotted);
            }

            @Override
            public void onError() {

            }
        });
    }
}
