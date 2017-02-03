package io.nearby.android.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Marc on 2017-01-29.
 */

public class Spotted {

    private int id;

    private String message;

    private double longitude;
    private double latitude;
    private LatLng latLng;

    public Spotted(String message) {
        this.message = message;
    }

    public Spotted(String message, double latitude, double longitude) {
        this.message = message;
        this.longitude = longitude;
        this.latitude = latitude;
        this.latLng = new LatLng(latitude,longitude);
    }

    public Spotted(String message, LatLng latLng) {
        this.message = message;
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
        this.latLng = latLng;
    }

    public boolean hasImage() {
        return false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LatLng getLatLng() {
        if(latLng == null){
            setLatLng(new LatLng(latitude,longitude));
        }

        return latLng;
    }

    private void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

}
