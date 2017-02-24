package io.nearby.android.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Marc on 2017-02-23.
 */
public class User {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("creationDate")
    @Expose
    private Date date;

    @SerializedName("disabled")
    @Expose
    private boolean disabled;

    @SerializedName("facebookId")
    @Expose
    private String facebookId;

    @SerializedName("facebookDate")
    @Expose
    private Date facebookDate;

    @SerializedName("googleId")
    @Expose
    private String googleId;

    @SerializedName("googleDate")
    @Expose
    private Date googleDate;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("profilePictureURL")
    @Expose
    private String profilePictureUrl;

    @SuppressWarnings("unused")
    public User() {   }

    public String getId() {
        return id;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public boolean hasFacebookAccount(){
        return facebookId != null;
    }

    public boolean hasGoogleAccount(){
        return googleId != null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", disabled=" + disabled +
                ", facebookId='" + facebookId + '\'' +
                ", facebookDate=" + facebookDate +
                ", googleId='" + googleId + '\'' +
                ", googleDate=" + googleDate +
                ", fullName='" + fullName + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                '}';
    }
}
