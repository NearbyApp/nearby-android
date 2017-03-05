package io.nearby.android.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Spotted implements Parcelable{

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

    /**
     * Constructor used when Spotted used as a Parcelable
     */
    public Spotted(Parcel source) {
        id = source.readString();
        userId = source.readString();
        message = source.readString();
        anonymous = source.readByte() != 1;
        pictureUrl = source.readString();
        fullName = source.readString();
        profilePictureUrl = source.readString();
        location = source.readParcelable(Location.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(message);
        dest.writeByte((byte) (anonymous ? 1 : 0));
        dest.writeString(pictureUrl);
        dest.writeString(fullName);
        dest.writeString(profilePictureUrl);
        dest.writeParcelable(location, flags);
    }

    public static final Parcelable.Creator<Spotted> CREATOR = new Creator<Spotted>() {
        @Override
        public Spotted createFromParcel(Parcel source) {
            return new Spotted(source);
        }

        @Override
        public Spotted[] newArray(int size) {
            return new Spotted[size];
        }
    };
}
