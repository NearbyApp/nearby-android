package io.nearby.android.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 2017-01-29.
 */

public class Spotted {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("anonymity")
    @Expose
    private boolean anonymity;

    private LatLng latLng;


    /*
     * Constructors
     */
    public Spotted(){}

    public Spotted(String message, double latitude, double longitude) {
        this(message,latitude,longitude,true);
    }

    public Spotted(String message, double latitude, double longitude, boolean anonymity){
        this.message = message;
        this.location = new Location(latitude, longitude);
        this.latLng = new LatLng(latitude,longitude);
        this.anonymity = anonymity;
    }

    public Spotted(String message, LatLng latLng) {
        this.message = message;
        this.location = new Location(latLng.latitude, latLng.longitude);
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
        return location.getLongitude();
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public boolean getAnonymity() {
        return anonymity;
    }

    public LatLng getLatLng() {
        if(latLng == null){
            setLatLng(new LatLng(getLatitude(),getLongitude()));
        }

        return latLng;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Spotted){
            Spotted spotted = (Spotted)obj;
            return spotted.getId().equals(id);
        }

        return super.equals(obj);
    }

    private void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    /**
     * Location class
     */
    private class Location{
        private static final int LATITUDE_INDEX = 0;
        private static final int LONGITUDE_INDEX = 1;

        @SerializedName("coordinates")
        @Expose
        private List<Double> coordinates;

        public Location() {
        }

        public Location(double lat, double lng){
            coordinates = new ArrayList<>();
            setCoordinates(lat, lng);
        }

        public double getLatitude(){
            return coordinates.get(LATITUDE_INDEX);
        }

        public double getLongitude(){
            return coordinates.get(LONGITUDE_INDEX);
        }

        public void setCoordinates(double lat, double lng){
            coordinates.clear();
            coordinates.add(lat);
            coordinates.add(lng);
        }

    }
}
