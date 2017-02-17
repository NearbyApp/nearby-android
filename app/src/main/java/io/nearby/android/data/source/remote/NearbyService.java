package io.nearby.android.data.source.remote;

import java.util.List;

import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.Remote;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Singleton
@Remote
public interface NearbyService {

    String ENDPOINT = "https://nbyapi.mo-bergeron.com/";

    @POST("/v1/login")
    Observable<Response<ResponseBody>> login();

    @GET("/v1/spotteds/me")
    Observable<List<Spotted>> getMySpotteds();

    @GET("/v1/spotteds")
    Observable<List<Spotted>> getSpotteds(@Query("minLat") double minLat,
                                          @Query("maxLat") double maxLat,
                                          @Query("minLong") double minLng,
                                          @Query("maxLong") double maxLng,
                                          @Query("locationOnly") boolean locationOnly);

    @FormUrlEncoded
    @POST("/v1/spotted")
    Observable<ResponseBody> createSpotted(@Field("anonymity") boolean anonymity,
                                           @Field("latitude") double lat,
                                           @Field("longitude") double lng,
                                           @Field("message") String message,
                                           @Field("picture") String pictureUrl);

    @GET("/v1/spotted/{spottedId}")
    Observable<Spotted> getSpotted(@Query("spottedId") String spottedId);

}
