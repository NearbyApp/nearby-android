package io.nearby.android.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import io.nearby.android.R;
import io.nearby.android.model.Spotted;
import io.nearby.android.util.BitmapUtil;

/**
 * Created by Marc on 2017-01-27.
 */

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {

    private final int FINE_LOCATION_PERMISSION_REQUEST = 9002;

    private GoogleMap mGoogleMap;
    private List<Marker> mMarkers = new ArrayList<>();
    private ClusterManager<SpottedClusterItem> mClusterManager;

    private boolean spottedClustered = true;

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
                if(permissions[0]== Manifest.permission.ACCESS_FINE_LOCATION &&
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
        mGoogleMap.setOnMyLocationButtonClickListener(this);

        if(spottedClustered){
            mClusterManager = new ClusterManager<>(getContext(),mGoogleMap);
            mClusterManager.setRenderer(new MapIconRenderer(getContext(),mGoogleMap,mClusterManager));
            mGoogleMap.setOnCameraIdleListener(mClusterManager);
            mGoogleMap.setOnMarkerClickListener(mClusterManager);

            addDummyClusteredSpotted();
        }
        else {
            mGoogleMap.setOnCameraIdleListener(this);
            mGoogleMap.setOnMarkerClickListener(this);

            addDummySpotted();
        }

        addMyLocationFeature();

    }

    private void addDummyClusteredSpotted(){
        // Set some lat/lng coordinates to start with.
        double lat = 45.4946433;
        double lng = -73.5627956;

        for (int i = 0; i < 25; i++) {
            double offset = i / 180d;
            lat = lat + offset;
            lng = lng + offset;
            Spotted spotted = new Spotted("Y ce passe quelque chose");
            SpottedClusterItem spottedClusterItem = new SpottedClusterItem(spotted,new LatLng(lat,lng));

            mClusterManager.addItem(spottedClusterItem);

        }
    }

    private void addDummySpotted(){
        // Set some lat/lng coordinates to start with.
        double lat = 45.4946433;
        double lng = -73.5627956;

        for (int i = 0; i < 25; i++) {
            double offset = i / 180d;
            lat = lat + offset;
            lng = lng + offset;
            Spotted spotted = new Spotted("Y ce passe quelque chose");

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(lat,lng))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.vectorDrawableToBitmap(getContext(), R.drawable.circle)))
                    .title("ETS");
            Marker marker = mGoogleMap.addMarker(markerOptions);
            marker.setTag(spotted);

            mMarkers.add(marker);
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

    /**
     * @return true if the listener has consumed the event (i.e., the default behavior should not
     * occur); false otherwise (i.e., the default behavior should occur). The default behavior is
     * for the camera move such that it is centered on the user location.
     */
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onCameraIdle() {
        Projection projection = mGoogleMap.getProjection();
        VisibleRegion visibleRegion = projection.getVisibleRegion();


        //  LatLngBounds{
        //      southwest=lat/lng: (-72.15103094986407,-72.32144739478825), --> Bottom-left
        //      northeast=lat/lng: (72.15105407200471,72.32141453772783) --> Top-right
        //  }
        LatLng northEast = visibleRegion.latLngBounds.northeast;
        LatLng southWest = visibleRegion.latLngBounds.southwest;

        //TODO Refresh spotted
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //TODO show the clicked spotted
        return false;
    }
}
