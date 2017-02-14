package io.nearby.android.ui.newspotted;

import io.nearby.android.data.remote.NearbyService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Marc on 2017-02-12.
 */

public class NewSpottedPresenter {

    private NewSpottedView mNewSpottedView;
    private NearbyService mNearbyService;

    private CompositeDisposable mCompositeDisposable;

    public NewSpottedPresenter(NewSpottedView newSpottedView, NearbyService nearbyService) {
        this.mNewSpottedView = newSpottedView;
        this.mNearbyService = nearbyService;

        mCompositeDisposable = new CompositeDisposable();
    }

    public void createSpotted(double lat, double lng, String message){
        //TODO retrieve anonymity setting in Preferences.
        boolean anonymity = true;


        Observable<ResponseBody> call = mNearbyService.createSpotted(true,lat,lng,message, null);
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        mNewSpottedView.onSpottedCreated();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mNewSpottedView.onSpottedNotCreated();
                    }
                });

        mCompositeDisposable.add(disposable);
    }
}
