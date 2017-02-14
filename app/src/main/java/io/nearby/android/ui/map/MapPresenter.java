package io.nearby.android.ui.map;

import java.util.List;

import io.nearby.android.data.model.Spotted;
import io.nearby.android.data.remote.NearbyService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-12.
 */

public class MapPresenter {

    private MapView mMapView;
    private NearbyService mNearbyService;

    private CompositeDisposable mCompositeDisposable;

    public MapPresenter(MapView mapView, NearbyService nearbyService) {
        mMapView = mapView;
        mNearbyService = nearbyService;

        mCompositeDisposable = new CompositeDisposable();
    }

    public void getSpotteds(double lat, double lng){
        boolean locationOnly = true;

        Observable<List<Spotted>> call = mNearbyService.getSpotteds(lat, lng, true);
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Spotted>>() {
                    @Override
                    public void accept(List<Spotted> spotteds) throws Exception {
                        mMapView.onSpottedsReceived(spotteds);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    public void getSpotted(final Spotted spotted){
        Observable<Spotted> call = mNearbyService.getSpotted(spotted.getId());
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Spotted>() {
                    @Override
                    public void accept(Spotted spotted) throws Exception {
                        mMapView.onSpottedDetailReceived(spotted);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                });

        mCompositeDisposable.add(disposable);
    }
}
