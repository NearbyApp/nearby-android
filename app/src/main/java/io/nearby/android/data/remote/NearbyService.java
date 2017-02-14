package io.nearby.android.data.remote;

import java.util.List;

import io.nearby.android.data.model.Spotted;
import io.nearby.android.data.model.SpottedListResponse;
import io.nearby.android.data.model.SpottedResponse;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
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
    Call<ResponseBody> login();

    @GET("/v1/spotteds/me")
    Observable<List<Spotted>> getMySpotteds();

    @FormUrlEncoded
    @GET("/v1/spotteds")
    Call<List<Spotted>> getSpotteds(@Field("latitude") double lat,
                                    @Field("longitude") double lng,
                                    @Field("locationOnly") boolean locationOnly);

    // TODO Fix anonymity when the server will be updated
    @FormUrlEncoded
    @POST("/v1/spotted")
    Call<ResponseBody> createSpotted(@Field("anonimity") boolean anonymity,
                                     @Field("latitude") double lat,
                                     @Field("longitude") double lng,
                                     @Field("message") String message,
                                     @Field("picture") String pictureUrl);

    @FormUrlEncoded
    @GET("/v1/spotted/{spottedId}")
    Call<SpottedResponse> getSpotted(@Path("spottedId") String spottedId);

}
