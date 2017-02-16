package io.nearby.android.data.source.remote;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.SpottedDataSource;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

import static dagger.internal.Preconditions.checkNotNull;

@Singleton
public class SpottedRemoteDataSource implements SpottedDataSource {

    private NearbyService mNearbyService;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public SpottedRemoteDataSource(@NonNull NearbyService nearbyService){
        checkNotNull(nearbyService);
        mNearbyService = nearbyService;

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void facebookLogin(String userId, String token, LoginCallback callback) {
        login(callback);
    }

    @Override
    public void googleLogin(String userId, String token, LoginCallback callback) {
        login(callback);
    }

    private void login(final LoginCallback callback) {
        Observable<Response<ResponseBody>> call = mNearbyService.login();
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        switch(response.code()){
                            case 200:
                                //Normal login
                                callback.onLoginSuccess();
                                break;
                            case 201:
                                // Account created
                                callback.onAccountCreated();
                                break;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                        callback.onError();
                    }
                });

        mCompositeDisposable.add(disposable);

    }

    @Override
    public void createSpotted(Spotted spotted,
                              Consumer<ResponseBody> onNext,
                              Consumer<Throwable> onError){
        Observable<ResponseBody> call = mNearbyService.createSpotted(true,
                spotted.getLatitude(),
                spotted.getLongitude(),
                spotted.getMessage(),
                null);
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
                /*
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
                */

        mCompositeDisposable.add(disposable);
    }
}
