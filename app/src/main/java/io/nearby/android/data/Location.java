package io.nearby.android.data;

/**
 * Created by Marc on 2017-03-05.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Location class
 */
class Location implements Parcelable {
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

    public Location(Parcel source){
        coordinates = new ArrayList<>();
        //Longitude
        coordinates.add(source.readDouble());
        //Latitude
        coordinates.add(source.readDouble());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(getLongitude());
        dest.writeDouble(getLatitude());
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
