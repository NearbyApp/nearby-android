package io.nearby.android.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import io.nearby.android.model.Spotted;

/**
 * Created by Marc on 2017-01-27.
 */

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private final int FINE_LOCATION_PERMISSION_REQUEST = 9002;

    private GoogleMap mGoogleMap;
    private NearbyClusterManager<SpottedClusterItem> mClusterManager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case FINE_LOCATION_PERMISSION_REQUEST:
                if(permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)&&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    addMyLocationFeature();
                }
                break;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add mMarkers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mClusterManager = new NearbyClusterManager<>(getContext(), mGoogleMap);

        //Setting a custom renderer to shot unique spotted with custom marker.
        MapIconRenderer renderer = new MapIconRenderer(getContext(),mGoogleMap,mClusterManager);
        mClusterManager.setRenderer(renderer);

        mGoogleMap.setOnMyLocationButtonClickListener(this);
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
        addDummyClusteredSpotted();

        addMyLocationFeature();

    }

    /**
     * @return true if the listener has consumed the event (i.e., the default behavior should not
     * occur); false otherwise (i.e., the default behavior should occur). The default behavior is
     * for the camera move such that it is centered on the user location.
     */
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    /**
     * Adds 25 dummy spotted
     */
    private void addDummyClusteredSpotted(){
        // Set some lat/lng coordinates to start with.
        double lat = 45.4946433;
        double lng = -73.5627956;

        for (int i = 0; i < 25; i++) {
            double offset = i / 180d;
            lat = lat + offset;
            lng = lng + offset;
            Spotted spotted = new Spotted("Y ce passe quelque chose",new LatLng(lat,lng));
            SpottedClusterItem spottedClusterItem = new SpottedClusterItem(spotted);

            mClusterManager.addItem(spottedClusterItem);
        }
    }

    private void addMyLocationFeature(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_PERMISSION_REQUEST);
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
    }
}
