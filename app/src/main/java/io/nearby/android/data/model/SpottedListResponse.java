package io.nearby.android.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Marc on 2017-02-12.
 */

public class SpottedListResponse {

    @SerializedName("result")
    List<Spotted> mSpotteds;


    public List<Spotted> getSpotted(){
        return  mSpotteds;
    }

    public void setSpotted(List<Spotted> spotteds){
        mSpotteds = spotteds;
    }


}
