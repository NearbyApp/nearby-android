package io.nearby.android.ui.map;

import java.util.List;

import javax.inject.Inject;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.ui.BasePresenter;

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View mView;
    private DataManager mDataManager;

    @Inject
    public MapPresenter(MapContract.View mapView, DataManager DataManager) {
        mView = mapView;
        mDataManager = DataManager;
    }

    @Inject
    void setupListeners(){
        mView.setPresenter(this);
    }

    @Override
    public void getSpotteds(double minLat, double maxLat,
                            double minLng, double maxLng){
        mDataManager.loadSpotted(minLat, maxLat,
                minLng, maxLng,
                true,
                new SpottedDataSource.SpottedLoadedCallback() {
            @Override
            public void onSpottedLoaded(List<Spotted> spotted) {
                mView.onSpottedsReceived(spotted);
            }

            @Override
            public void onError(SpottedDataSource.ErrorType errorType) {
                if(!BasePresenter.manageError(mView, errorType)){
                    // Do nothing
                }
            }
        });
    }
}
