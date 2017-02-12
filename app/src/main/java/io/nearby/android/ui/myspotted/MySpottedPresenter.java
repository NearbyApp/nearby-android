package io.nearby.android.ui.myspotted;

import java.util.List;

import io.nearby.android.data.model.Spotted;
import io.nearby.android.data.model.SpottedListResponse;
import io.nearby.android.data.remote.NearbyService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-08.
 */

public class MySpottedPresenter {

    private MySpottedView mMySpottedView;
    private NearbyService mNearbyService;

    public MySpottedPresenter(MySpottedView mySpottedView, NearbyService nearbyService){
        mMySpottedView = mySpottedView;
        mNearbyService = nearbyService;
    }

    public void loadMySpotted(){
        Call<SpottedListResponse> call = mNearbyService.getMySpotteds();
        call.enqueue(new Callback<SpottedListResponse>() {
            @Override
            public void onResponse(Call<SpottedListResponse> call, Response<SpottedListResponse> response) {
                SpottedListResponse spottedListResponse = response.body();
                mMySpottedView.onMySpottedReceived(spottedListResponse.getSpotted());
            }

            @Override
            public void onFailure(Call<SpottedListResponse> call, Throwable t) {
                Timber.e(t);
            }
        });
    }

    public void refreshMySpotted(){

    }

    public void loadMyOlderSpotted(Spotted lastSpotted){
        String id = lastSpotted.getId();

        //TODO Add a call to get my older spotted
        //mNearbyService.getMySpotteds(id);
    }


}
