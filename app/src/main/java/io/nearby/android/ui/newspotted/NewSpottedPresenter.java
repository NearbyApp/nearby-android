package io.nearby.android.ui.newspotted;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.remote.NearbyService;
import io.nearby.android.ui.Presenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

@Singleton
public class NewSpottedPresenter implements NewSpottedContract.Presenter{

    private NewSpottedContract.View mNewSpottedView;
    private DataManager mDataManager;

    @Inject
    public NewSpottedPresenter(NewSpottedContract.View newSpottedView, DataManager dataManager) {
        this.mNewSpottedView = newSpottedView;
        this.mDataManager = dataManager;
    }

    @Inject
    void setupListeners(){ mNewSpottedView.setPresenter(this);}

    public void createSpotted(double lat, double lng, String message){
        //TODO retrieve anonymity setting in Preferences.
        Spotted spotted = new Spotted(message,lat,lng);
        spotted.setAnonymity(true);


        mDataManager.createSpotted(spotted,new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                mNewSpottedView.onSpottedCreated();
            }},new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    mNewSpottedView.onSpottedNotCreated();
                }
            });
    }
}
