package io.nearby.android.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.nearby.android.data.local.SharedPreferencesHelper;
import io.nearby.android.data.model.Spotted;
import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Marc on 2017-02-07.
 */

public interface NearbyService {

    String ENDPOINT = "https://nbyapi.mo-bergeron.com/";

    @GET("/v1/spotteds/me")
    Observable<List<Spotted>> getMySpotted();

    @POST("/v1/login/facebook")
    Call<ResponseBody> loginWithFacebook();

    @POST("/v1/login/google")
    Call loginWithGoogle();

    class Builder {

        private static final String AUTHORIZATION_HEADER = "Authorization";
        private static final String SERVICE_PROVIDER_HEADER = "Service-Provider";

        private SharedPreferencesHelper mSharedPreferenceHelper;

        public Builder(){ }

        public NearbyService build(){
            Gson gson = new GsonBuilder()
                    .create();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //TODO Fix ssl problem
            //builder.sslSocketFactory(SSLSocketFactory.getDefault())

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();

                    Request.Builder requestBuilder = originalRequest.newBuilder();

                    int lastSignInMethod = mSharedPreferenceHelper.getLastSignInMethod();

                    String token = "";
                    String provider = "";

                    switch(lastSignInMethod){
                        case SharedPreferencesHelper.LAST_SIGN_IN_METHOD_FACEBOOK:
                            provider = "facebook";
                            token = mSharedPreferenceHelper.getFacebookToken();
                            break;
                        case SharedPreferencesHelper.LAST_SIGN_IN_METHOD_GOOGLE:
                            provider = "google";
                            token = mSharedPreferenceHelper.getGoogleToken();
                            break;
                    }

                    //TODO Validate that the user is logged in
                    requestBuilder.header(AUTHORIZATION_HEADER,token);
                    requestBuilder.addHeader(SERVICE_PROVIDER_HEADER, provider);

                    Request request = requestBuilder.build();

                    return chain.proceed(request);
                }
            });

            OkHttpClient client = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit.create(NearbyService.class);
        }

        public void addSharedPreferencesHelper(SharedPreferencesHelper sharedPreferencesHelper) {
            mSharedPreferenceHelper = sharedPreferencesHelper;
        }
    }
}
