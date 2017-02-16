package io.nearby.android.data.source.remote;

import java.util.List;

import io.nearby.android.data.Spotted;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Marc on 2017-02-07.
 */

public interface NearbyService {

    String ENDPOINT = "https://nbyapi.mo-bergeron.com/";

    @POST("/v1/login")
    Observable<Response<ResponseBody>> login();

    @GET("/v1/spotteds/me")
    Observable<List<Spotted>> getMySpotteds();

    @FormUrlEncoded
    @GET("/v1/spotteds")
    Observable<List<Spotted>> getSpotteds(@Field("latitude") double lat,
                                          @Field("longitude") double lng,
                                          @Field("locationOnly") boolean locationOnly);

    @FormUrlEncoded
    @POST("/v1/spotted")
    Observable<ResponseBody> createSpotted(@Field("anonymity") boolean anonymity,
                                           @Field("latitude") double lat,
                                           @Field("longitude") double lng,
                                           @Field("message") String message,
                                           @Field("picture") String pictureUrl);

    @FormUrlEncoded
    @GET("/v1/spotted/{spottedId}")
    Observable<Spotted> getSpotted(@Path("spottedId") String spottedId);

}
