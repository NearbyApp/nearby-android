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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.nearby.android.R;
import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.remote.NearbyService;
import io.nearby.android.google.GoogleApiClientBuilder;
import io.nearby.android.google.maps.MapIconRenderer;
import io.nearby.android.google.maps.NearbyClusterManager;
import io.nearby.android.google.maps.SpottedClusterItem;
import io.nearby.android.ui.base.BaseFragment;
import io.nearby.android.ui.newspotted.NewSpottedActivity;

/**
 * Created by Marc on 2017-01-27.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        View.OnClickListener,
        MapView, GoogleApiClient.ConnectionCallbacks {

    private final int FINE_LOCATION_PERMISSION_REQUEST = 9002;
    private final String PARAMS_MAP_CAMERA_POSITION = "PARAMS_MAP_CAMERA_POSITION";

    private MapPresenter mPresenter;
    private GoogleMap mGoogleMap;
    private NearbyClusterManager<SpottedClusterItem> mClusterManager;

    private GoogleApiClient mGoogleApiClient;
    @Inject NearbyService mNearbyService;
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

        mComponent.inject(this);

        mPresenter = new MapPresenter(this, mNearbyService);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.map_fragment, viewGroup, false);

        view.findViewById(R.id.fab).setOnClickListener(this);

        if(savedInstanceState != null){
            mMapInitCamPos = savedInstanceState.getParcelable(PARAMS_MAP_CAMERA_POSITION);
        }
        else if(mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClientBuilder.buildLocationApiclient(this.getActivity(), this, null);
        }

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        getChildFragmentManager().beginTransaction().add(R.id.support_map_fragment, mapFragment).commit();

        return view;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapInitCamPos = mGoogleMap.getCameraPosition();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
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
        MapIconRenderer<SpottedClusterItem> renderer = new MapIconRenderer<>(getContext(),mGoogleMap,mClusterManager);
        mClusterManager.setRenderer(renderer);

        mGoogleMap.setOnMyLocationButtonClickListener(this);
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
        addDummyClusteredSpotted();

        addMyLocationFeature();

        if(mMapInitCamPos != null){
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mMapInitCamPos));
        }
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
        List<SpottedClusterItem> clusterItems = new ArrayList<>();
        for (Spotted spotted : spotteds) {
            clusterItems.add(new SpottedClusterItem(spotted));
        }
        mClusterManager.addItems(clusterItems);
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
