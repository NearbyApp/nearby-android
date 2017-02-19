package io.nearby.android.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.data.Spotted;
import io.nearby.android.google.GoogleApiClientBuilder;
import io.nearby.android.google.maps.NearbyClusterManager;
import io.nearby.android.google.maps.SpottedClusterItem;
import io.nearby.android.ui.newspotted.NewSpottedActivity;

/**
 * Created by Marc on 2017-01-27.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        View.OnClickListener,
        MapContract.View,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnCameraIdleListener {

    private final int FINE_LOCATION_PERMISSION_REQUEST = 9002;
    private final String PARAMS_MAP_CAMERA_POSITION = "PARAMS_MAP_CAMERA_POSITION";

    @Inject MapPresenter mPresenter;
    private GoogleMap mGoogleMap;
    private NearbyClusterManager<SpottedClusterItem> mClusterManager;
    private List<Spotted> mSpotteds;

    private GoogleApiClient mGoogleApiClient;
    private CameraPosition mMapInitCamPos;

    public static MapFragment newInstance() {

        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpotteds = new ArrayList<>();

        setRetainInstance(true);

        DaggerMapComponent.builder()
                .mapPresenterModule(new MapPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getActivity().getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.map_fragment, viewGroup, false);

        view.findViewById(R.id.fab).setOnClickListener(this);

        if(savedInstanceState != null){
            mMapInitCamPos = savedInstanceState.getParcelable(PARAMS_MAP_CAMERA_POSITION);
        }
        else if(mGoogleApiClient == null) {
            //enableAutoManage can't be used because it would be binded with the MainActivity.
            mGoogleApiClient = new GoogleApiClientBuilder(getContext())
                    .addLocationServicesApi()
                    .addConnectionCallbacks(this)
                    .build();
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.support_map_fragment);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapInitCamPos = mGoogleMap.getCameraPosition();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case FINE_LOCATION_PERMISSION_REQUEST:
                if(permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)&&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    addMyLocationFeature();

                    if(mMapInitCamPos != null){
                        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mMapInitCamPos));
                    }
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
        mClusterManager.setOnCameraIdleListener(this);

        mGoogleMap.setOnMyLocationButtonClickListener(this);
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);

        addMyLocationFeature();

        if(mMapInitCamPos != null){
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mMapInitCamPos));
        }
    }

    @Override
    public void onCameraIdle() {
        Projection projection = mGoogleMap.getProjection();
        VisibleRegion visibleRegion = projection.getVisibleRegion();

        LatLng northeast = visibleRegion.latLngBounds.northeast;
        LatLng southWest = visibleRegion.latLngBounds.southwest;

        mPresenter.getSpotteds(southWest.latitude,northeast.latitude,southWest.longitude,northeast.longitude);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent intent = new Intent(getActivity(),NewSpottedActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSpottedsReceived(List<Spotted> spotteds) {
        for (Spotted spotted : spotteds) {
            if(!mSpotteds.contains(spotted)){
                mSpotteds.add(spotted);
                SpottedClusterItem item = new SpottedClusterItem(spotted);
                mClusterManager.addItem(item);
            }
        }

        mClusterManager.cluster();
    }

    @Override
    public void onSpottedDetailReceived(Spotted spotted) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double lat = lastLocation.getLatitude();
        double lng = lastLocation.getLongitude();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat,lng))
                .zoom(17)
                .build();

        if(mGoogleMap != null){
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else if(mMapInitCamPos == null){
            mMapInitCamPos = cameraPosition;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = (MapPresenter) presenter;
    }

    private void addMyLocationFeature() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION_REQUEST);
            return;
        }

        mGoogleMap.setMyLocationEnabled(true);
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
}
