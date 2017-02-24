package io.nearby.android.data.source.remote;

import java.util.List;

import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.Remote;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("/v1/spotteds/me")
    Observable<List<Spotted>> getMyOlderSpotteds(@Query("skip") int skip);

    @GET("/v1/spotteds/me")
    Observable<List<Spotted>> getMyNewerSpotteds(@Query("since") int since);

    @GET("/v1/spotteds")
    Observable<List<Spotted>> getSpotteds(@Query("minLat") double minLat,
                                          @Query("maxLat") double maxLat,
                                          @Query("minLong") double minLng,
                                          @Query("maxLong") double maxLng,
                                          @Query("locationOnly") boolean locationOnly);

    @Multipart
    @POST("/v1/spotted")
    Observable<ResponseBody> createSpotted(@Part("anonymity") RequestBody anonymity,
                                           @Part("latitude") RequestBody lat,
                                           @Part("longitude") RequestBody lng,
                                           @Part("message") RequestBody message,
                                           @Part MultipartBody.Part pictureFile);

    @Multipart
    @POST("/v1/spotted")
    Observable<ResponseBody> createSpotted(@Part("anonymity") RequestBody anonymity,
                                           @Part("latitude") RequestBody lat,
                                           @Part("longitude") RequestBody lng,
                                           @Part("message") RequestBody message);


    @GET("/v1/spotted/{spottedId}")
    Observable<Spotted> getSpotted(@Path("spottedId") String spottedId);

    @GET("/v1/user/me")
    Observable<ResponseBody> getUser();

    @FormUrlEncoded
    @POST("/v1/link/facebook")
    Observable<ResponseBody> linkFacebookAccount(@Field("facebookId") String facebookId,
                                                 @Field("token") String token);

    @FormUrlEncoded
    @POST("/v1/link/google")
    Observable<ResponseBody> linkGoogleAccount(@Field("googleId") String googleId,
                                               @Field("token") String token);

    @POST("/v1/disable")
    Observable<ResponseBody> deactivateAccount();
}
