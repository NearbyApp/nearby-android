package io.nearby.android.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marc on 2017-02-12.
 */

public class SpottedResponse {

    @SerializedName("result")
    @Expose
    private Spotted mSpotted;

    public Spotted getSpotted() {
        return mSpotted;
    }

    public void setSpotted(Spotted spotted) {
        this.mSpotted = spotted;
    }
}
