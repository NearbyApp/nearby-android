package io.nearby.android.ui.map;

import java.util.List;

import javax.inject.Inject;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.data.source.remote.NearbyService;
import io.nearby.android.ui.Presenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-12.
 */

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

    public void getSpottedDetails(Spotted spotted){
        mDataManager.loadSpottedDetails(spotted, new SpottedDataSource.SpottedDetailsLoadedCallback() {
            @Override
            public void onSpottedDetailsLoaded(Spotted spotted) {
                mMapView.onSpottedDetailReceived(spotted);
            }

            @Override
            public void onError() {
                // TODO manage error
            }
        });
    }
}
