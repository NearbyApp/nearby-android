package io.nearby.android.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.User;
import io.nearby.android.data.source.Remote;
import io.nearby.android.data.source.SpottedDataSource;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

import static dagger.internal.Preconditions.checkNotNull;

@Singleton
@Remote
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
    public void isUserLoggedIn(UserLoginStatusCallback callback) {
        callback.userIsNotLoggedIn();
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
        final Observable<Response<ResponseBody>> call = mNearbyService.login();
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
                            default:
                                throw new HttpException(response);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);

    }

    @Override
    public void createSpotted(Spotted spotted,
                              @Nullable File image,
                              final SpottedCreatedCallback callback){

        RequestBody latitude = RequestBody.create(MultipartBody.FORM, Double.toString(spotted.getLatitude()));
        RequestBody longitude = RequestBody.create(MultipartBody.FORM, Double.toString(spotted.getLongitude()));
        RequestBody message = RequestBody.create(MultipartBody.FORM, spotted.getMessage());
        RequestBody anonymity = RequestBody.create(MultipartBody.FORM, Boolean.toString(spotted.isAnonymous()));

        Observable<ResponseBody> call;
        if(image != null){
            RequestBody requestImage = RequestBody.create(MediaType.parse("image/*"),image);
            MultipartBody.Part body = MultipartBody.Part.createFormData("picture",image.getName(),requestImage);

            call = mNearbyService.createSpotted(anonymity,
                    latitude,
                    longitude,
                    message,
                    body);
        }
        else {
            call = mNearbyService.createSpotted(anonymity,
                    latitude,
                    longitude,
                    message);
        }

        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        callback.onSpottedCreated();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadMySpotted(final MySpottedLoadedCallback callback) {
        Observable<List<Spotted>> call = mNearbyService.getMySpotteds();

        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Spotted>>() {
                    @Override
                    public void accept(List<Spotted> spotteds) throws Exception {
                        callback.onMySpottedLoaded(spotteds);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadMyOlderSpotted(int spottedCount, final MySpottedLoadedCallback callback) {
        Observable<List<Spotted>> call = mNearbyService.getMyOlderSpotteds(spottedCount);

        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Spotted>>() {
                    @Override
                    public void accept(List<Spotted> spotteds) throws Exception {
                        callback.onMySpottedLoaded(spotteds);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getMyNewerSpotteds(Date myOlderSpotted, final MySpottedLoadedCallback callback) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Observable<List<Spotted>> call = mNearbyService.getMyNewerSpotteds(dateFormat.format(myOlderSpotted));
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Spotted>>() {
                    @Override
                    public void accept(List<Spotted> spotteds) throws Exception {
                        callback.onMySpottedLoaded(spotteds);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadSpotted(double minLat, double maxLat,
                            double minLng, double maxLng,
                            boolean locationOnly,
                            final SpottedLoadedCallback callback) {
        Observable<List<Spotted>> call = mNearbyService.getSpotteds(minLat, maxLat, minLng, maxLng, locationOnly);
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Spotted>>() {
                    @Override
                    public void accept(List<Spotted> spotteds) throws Exception {
                        callback.onSpottedLoaded(spotteds);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadSpottedDetails(String spottedId, final SpottedDetailsLoadedCallback callback) {
        Observable<Spotted> call = mNearbyService.getSpotted(spottedId);
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Spotted>() {
                    @Override
                    public void accept(Spotted spotted) throws Exception {
                    callback.onSpottedDetailsLoaded(spotted);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public boolean getDefaultAnonymity() {
        return false;
    }

    @Override
    public void setDefaultAnonymity(boolean anonymity) {
        //Do nothing
    }

    @Override
    public void getUserInfo(final UserInfoLoadedCallback callback) {
        Observable<User> call = mNearbyService.getUser();
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        callback.onUserInfoLoaded(user);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void linkFacebookAccount(final String userId, final String token, final FacebookLinkAccountCallback callback) {
        Observable<ResponseBody> call = mNearbyService.linkFacebookAccount(userId, token);
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                callback.onSuccess();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                boolean exceptionHandled = false;

                if (throwable instanceof HttpException) {
                    HttpException exception = (HttpException) throwable;
                    if (exception.code() == 403) {
                        exceptionHandled = true;
                        callback.onFacebookAccountAlreadyExist(userId, token);
                    }
                }

                if (!exceptionHandled) {
                    manageError(throwable, callback);
                }
            }
        });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void linkGoogleAccount(final String userId, final String token, final GoogleLinkAccountCallback callback) {
        Observable<ResponseBody> call = mNearbyService.linkGoogleAccount(userId, token);
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        callback.onSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        boolean exceptionHandled = false;

                        if(throwable instanceof HttpException){
                            HttpException exception = (HttpException) throwable;
                            if(exception.code() == 403){
                                exceptionHandled = true;
                                callback.onGoogleAccountAlreadyExist(userId, token);
                            }
                        }

                        if(!exceptionHandled) {
                            manageError(throwable, callback);
                        }
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void mergeFacebookAccount(String userId, String token, final Callback callback) {
        Observable<ResponseBody> call = mNearbyService.mergeFacebookAccount(userId, token);
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                       @Override
                       public void accept(ResponseBody responseBody) throws Exception {
                           callback.onSuccess();
                       }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void mergeGoogleAccount(String userId, String token, final Callback callback) {
        Observable<ResponseBody> call = mNearbyService.mergeGoogleAccount(userId, token);
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                       @Override
                       public void accept(ResponseBody responseBody) throws Exception {
                           callback.onSuccess();
                       }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void signOut(Callback callback) {
        //Do nothing
    }

    @Override
    public void deactivateAccount(final Callback callback) {
        Observable<ResponseBody> call = mNearbyService.deactivateAccount();
        Disposable disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        callback.onSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        manageError(throwable, callback);
                    }
                });

        mCompositeDisposable.add(disposable);
    }


    private void manageError(Throwable throwable, ErrorCallback callback) {
        Timber.e(throwable);

        if(throwable instanceof  HttpException){
            HttpException exception = (HttpException) throwable;

            switch(exception.code()){
                case 400:
                case 403:
                case 404:
                case 405:
                case 500:
                    callback.onError(ErrorType.Other);
                    break;
                case 401:
                    callback.onError(ErrorType.UnauthorizedUser);
                    break;
                case 410:
                    callback.onError(ErrorType.DisabledUser);
                    break;
                default:
                    callback.onError(ErrorType.Other);
                    break;
            }
        }
        else {
            callback.onError(ErrorType.Other);
        }
    }
}
