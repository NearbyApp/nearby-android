package io.nearby.android.ui.myspotted;

import java.util.List;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.remote.NearbyService;
import io.nearby.android.ui.Presenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-08.
 */

public class MySpottedPresenter extends Presenter{

    private MySpottedView mMySpottedView;
    private NearbyService mNearbyService;

    public MySpottedPresenter(MySpottedView mySpottedView, NearbyService nearbyService){
        mMySpottedView = mySpottedView;
        mNearbyService = nearbyService;
    }

    public void loadMySpotted(){
        Observable<List<Spotted>> call = mNearbyService.getMySpotteds();

        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Spotted>>() {
                    @Override
                    public void accept(List<Spotted> spotteds) throws Exception {
                        mMySpottedView.onMySpottedReceived(spotteds);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);

                    }
                });

        mCompositeDisposable.add(disposable);

    }

    public void refreshMySpotted(){

    }

    public void loadMyOlderSpotted(Spotted lastSpotted){
        String id = lastSpotted.getId();

        //TODO Add a call to get my older spotted
        //mNearbyService.getMySpotteds(id);
    }
}
