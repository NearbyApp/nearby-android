package io.nearby.android.ui.newspotted;

import io.nearby.android.data.remote.NearbyService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Marc on 2017-02-12.
 */

public class NewSpottedPresenter {

    private NewSpottedView mNewSpottedView;
    private NearbyService mNearbyService;

    public NewSpottedPresenter(NewSpottedView newSpottedView, NearbyService nearbyService) {
        this.mNewSpottedView = newSpottedView;
        this.mNearbyService = nearbyService;
    }

    public void createSpotted(double lat, double lng, String message){
        //TODO retrieve anonymity setting in Preferences.
        boolean anonymity = true;

        Call<ResponseBody> call = mNearbyService.createSpotted(true,lat,lng,message, null);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mNewSpottedView.onSpottedCreated();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
