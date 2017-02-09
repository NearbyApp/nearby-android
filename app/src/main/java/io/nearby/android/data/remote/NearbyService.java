package io.nearby.android.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.List;

import io.nearby.android.data.model.Spotted;
import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    void loginWithFacebook();

    @POST("/v1/login/google")
    void loginWithGoogle();

    class Creator{
        public static NearbyService provideNearbyService(){
            Gson gson = new GsonBuilder()
                    .create();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();

                    Request.Builder requestBuilder = originalRequest.newBuilder();

                    //TODO Validate that the user is logged in
                    requestBuilder.header("Authorization","");
                    requestBuilder.addHeader("Provider","");

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
    }
}
