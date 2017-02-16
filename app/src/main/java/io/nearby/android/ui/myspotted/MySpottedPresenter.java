package io.nearby.android.ui.myspotted;

import java.util.List;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;

/**
 * Created by Marc on 2017-02-08.
 */

public class MySpottedPresenter implements MySpottedContract.Presenter{

    private MySpottedContract.View mMySpottedView;
    private DataManager mDataManager;

    public MySpottedPresenter(MySpottedContract.View mySpottedView, DataManager dataManager){
        mMySpottedView = mySpottedView;
        mDataManager = dataManager;
    }

    @Override
    public void loadMySpotted(){
        mDataManager.loadMySpotted(new SpottedDataSource.MySpottedLoadedCallback() {
            @Override
            public void onMySpottedLoaded(List<Spotted> mySpotted) {
                mMySpottedView.onMySpottedReceived(mySpotted);
            }

            @Override
            public void onError() {
                //TODO Manage errors
            }
        });
    }

    public void refreshMySpotted(){

    }

    public void loadMyOlderSpotted(Spotted lastSpotted){
        String id = lastSpotted.getId();

        //TODO Add a call to get my older spotted
        //mNearbyService.getMySpotteds(id);
    }
}
