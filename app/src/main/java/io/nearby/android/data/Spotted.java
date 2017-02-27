package io.nearby.android.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Marc on 2017-01-29.
 */

public class Spotted {

    public static final String DEFAULT_ID = "0";

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
    private boolean anonymous;

    @SerializedName("creationDate")
    @Expose
    private Date creationDate;

    @SerializedName("pictureURL")
    @Expose
    private String pictureUrl;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("profilePictureURL")
    @Expose
    private String profilePictureUrl;

    private LatLng latLng;


    public Spotted(){}

    public Spotted(String id, String message, double latitude, double longitude) {
        this(id,message,latitude,longitude,true);
    }

    public Spotted(String id, String message, double latitude, double longitude, boolean anonymous){
        this.id = id;
        this.message = message;
        this.location = new Location(latitude, longitude);
        this.latLng = new LatLng(latitude,longitude);
        this.anonymous = anonymous;
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

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public LatLng getLatLng() {
        if(latLng == null){
            setLatLng(new LatLng(getLatitude(),getLongitude()));
        }

        return latLng;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Spotted){
            Spotted spotted = (Spotted)obj;
            if(spotted.getId() != null){
                return spotted.getId().equals(id);
            }
        }

        return super.equals(obj);
    }

    private void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return "Spotted{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", message='" + message + '\'' +
                ", location=" + location +
                ", anonymous=" + anonymous +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", fullName='" + fullName + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                '}';
    }

    /**
     * Location class
     */
    private class Location{
        private static final int LONGITUDE_INDEX = 0;
        private static final int LATITUDE_INDEX = 1;

        @SerializedName("coordinates")
        @Expose
        private List<Double> coordinates;

        @SuppressWarnings("unused")
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

            // The order here is really important
            coordinates.add(lng);
            coordinates.add(lat);
        }

    }
}
