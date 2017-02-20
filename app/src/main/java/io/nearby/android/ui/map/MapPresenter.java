package io.nearby.android.ui.map;

import java.util.List;

import javax.inject.Inject;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View mMapView;
    private DataManager mDataManager;

    @Inject
    public MapPresenter(MapContract.View mapView, DataManager DataManager) {
        mMapView = mapView;
        mDataManager = DataManager;
    }

    @Inject
    void setupListeners(){
        mMapView.setPresenter(this);
    }

    @Override
    public void getSpotteds(double minLat, double maxLat,
                            double minLng, double maxLng){
        boolean locationOnly = true;

        mDataManager.loadSpotted(minLat, maxLat,
                minLng, maxLng,
                locationOnly,
                new SpottedDataSource.SpottedLoadedCallback() {
            @Override
            public void onSpottedLoaded(List<Spotted> spotted) {
                mMapView.onSpottedsReceived(spotted);
            }

            @Override
            public void onError() {
                // TODO manage error
            }
        });
    }
}
