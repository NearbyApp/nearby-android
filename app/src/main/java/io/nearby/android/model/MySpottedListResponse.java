package io.nearby.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 2017-02-07.
 */
public class MySpottedListResponse {

    @SerializedName("data")
    @Expose
    private List<Spotted> data = new ArrayList<>();

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status")
    @Expose
    private int status;

    public List<Spotted> getData() {
        return data;
    }

    public void setData(List<Spotted> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
