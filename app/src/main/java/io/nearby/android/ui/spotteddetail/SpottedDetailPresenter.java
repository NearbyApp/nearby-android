package io.nearby.android.ui.spotteddetail;

import javax.inject.Inject;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;

public class SpottedDetailPresenter implements SpottedDetailContract.Presenter {

    private SpottedDetailContract.View mView;
    private DataManager mDataManager;

    @Inject
    public SpottedDetailPresenter(SpottedDetailContract.View view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
    }

    @Inject
    void setupListeners(){
        mView.setPresenter(this);
    }

    @Override
    public void loadSpottedDetails(String spottedId) {
        mDataManager.loadSpottedDetails(spottedId, new SpottedDataSource.SpottedDetailsLoadedCallback() {
            @Override
            public void onSpottedDetailsLoaded(Spotted spotted) {
                mView.onSpottedDetailsReceived(spotted);
            }

            @Override
            public void onError() {

            }
        });
    }
}
