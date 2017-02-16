package io.nearby.android.data.source;

import android.support.annotation.NonNull;

import io.nearby.android.data.Spotted;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Marc on 2017-02-16.
 */

public interface SpottedDataSource {

    interface LoginCallback{
        void onAccountCreated();
        void onLoginSuccess();
        void onError();
    }

    void facebookLogin(String userId, String token, LoginCallback callback);

    void googleLogin(String userId, String token, LoginCallback callback);

    void createSpotted(@NonNull Spotted spotted, Consumer<ResponseBody> onNext, Consumer<Throwable> onError);
}
