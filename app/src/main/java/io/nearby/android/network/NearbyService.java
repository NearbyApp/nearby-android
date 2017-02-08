package io.nearby.android.network;

import io.nearby.android.model.MySpottedListResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Marc on 2017-02-07.
 */

public interface NearbyService {

    @GET("v1/spotteds/")
    Observable<MySpottedListResponse> getMySpotteds();
}
