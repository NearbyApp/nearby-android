package io.nearby.android.ui.map;

import java.util.List;

import io.nearby.android.data.model.Spotted;
import io.nearby.android.data.model.SpottedListResponse;
import io.nearby.android.data.model.SpottedResponse;
import io.nearby.android.data.remote.NearbyService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-12.
 */

public class MapPresenter {

    private MapView mMapView;
    private NearbyService mNearbyService;

    public MapPresenter(MapView mapView, NearbyService nearbyService) {
        mMapView = mapView;
        mNearbyService = nearbyService;
    }

    public void getSpotteds(double lat, double lng){
        Call<List<Spotted>> call = mNearbyService.getSpotteds(lat, lng, true);
        call.enqueue(new Callback<List<Spotted>>() {
            @Override
            public void onResponse(Call<List<Spotted>> call, Response<List<Spotted>> response) {
                List<Spotted> spottedListResponse = response.body();
                mMapView.onSpottedsReceived(spottedListResponse);
            }

            @Override
            public void onFailure(Call<List<Spotted>> call, Throwable t) {
                Timber.e(t);
            }
        });
    }

    public void getSpotted(final Spotted spotted){
        Call<SpottedResponse> call = mNearbyService.getSpotted(spotted.getId());
        call.enqueue(new Callback<SpottedResponse>() {
            @Override
            public void onResponse(Call<SpottedResponse> call, Response<SpottedResponse> response) {
                SpottedResponse spottedResponse = response.body();
                mMapView.onSpottedDetailReceived(spottedResponse.getSpotted());
            }

            @Override
            public void onFailure(Call<SpottedResponse> call, Throwable t) {
                Timber.e(t);
            }
        });
    }
}
