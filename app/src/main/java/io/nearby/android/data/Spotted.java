package io.nearby.android.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marc on 2017-01-29.
 */

public class Spotted {

    @SerializedName("spottedId")
    @Expose
    private String id;

    @SerializedName("userId")
    @Expose
    private String userId;

    @Expose
    private String message;

    @SerializedName("longitude")
    @Expose
    private double lng;

    @SerializedName("latitude")
    @Expose
    private double lat;

    private LatLng latLng;


    /*
     * Constructors
     */
    public Spotted(){}

    public Spotted(String message) {
        this.message = message;
    }

    public Spotted(String message, double latitude, double longitude) {
        this.message = message;
        this.lng = longitude;
        this.lat = latitude;
        this.latLng = new LatLng(latitude,longitude);
    }

    public Spotted(String message, LatLng latLng) {
        this.message = message;
        this.lat = latLng.latitude;
        this.lng= latLng.longitude;
        this.latLng = latLng;
    }

    public boolean hasImage() {
        return false;
    }

    /*
     * Getter and setter
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getLongitude() {
        return lng;
    }

    public void setLongitude(double longitude) {
        this.lng = longitude;
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(double latitude) {
        this.lat = latitude;
    }

    public LatLng getLatLng() {
        if(latLng == null){
            setLatLng(new LatLng(lat,lng));
        }

        return latLng;
    }

    private void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

}
