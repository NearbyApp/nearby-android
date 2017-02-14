package io.nearby.android.ui.myspotted;

import io.nearby.android.data.model.Spotted;
import io.nearby.android.data.model.SpottedListResponse;
import io.nearby.android.data.remote.NearbyService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-08.
 */

public class MySpottedPresenter {

    private MySpottedView mMySpottedView;
    private NearbyService mNearbyService;

    private CompositeDisposable mCompositeDisposable;

    public MySpottedPresenter(MySpottedView mySpottedView, NearbyService nearbyService){
        mMySpottedView = mySpottedView;
        mNearbyService = nearbyService;

        mCompositeDisposable = new CompositeDisposable();
    }

    public void loadMySpotted(){
        Observable<SpottedListResponse> call = mNearbyService.getMySpotteds();

        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SpottedListResponse>() {
                    @Override
                    public void accept(SpottedListResponse spottedListResponse) throws Exception {
                        mMySpottedView.onMySpottedReceived(spottedListResponse.getSpotted());
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
