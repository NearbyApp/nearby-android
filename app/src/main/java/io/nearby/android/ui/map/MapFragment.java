package io.nearby.android.ui.map;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
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
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.data.Spotted;
import io.nearby.android.google.GoogleApiClientBuilder;
import io.nearby.android.google.maps.NearbyClusterManager;
import io.nearby.android.google.maps.SpottedClusterItem;
import io.nearby.android.ui.BaseFragment;
import io.nearby.android.ui.newspotted.NewSpottedActivity;
import io.nearby.android.ui.spottedclusterdetail.SpottedClusterDetailActivity;
import io.nearby.android.ui.spotteddetail.SpottedDetailActivity;
import timber.log.Timber;

public class MapFragment extends BaseFragment<MapContract.Presenter> implements OnMapReadyCallback,
        MapContract.View,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnCameraIdleListener,
        GoogleApiClient.OnConnectionFailedListener,
        ClusterManager.OnClusterItemClickListener<SpottedClusterItem> {

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002;
    private static final LatLng DEFAULT_LOCATION = new LatLng(45.5015537,-73.5674999);

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private NearbyClusterManager<SpottedClusterItem> mClusterManager;
    private SupportMapFragment mMapFragment;

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private boolean mMapInitialized = false;

    @Inject MapPresenter mPresenter;

    public static MapFragment newInstance() {

        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = (MapPresenter) presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMapComponent.builder()
                .mapPresenterModule(new MapPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getActivity().getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);

        mMapFragment =  SupportMapFragment.newInstance();
        mMapInitialized = false;

        //enableAutoManage can't be used because it would be binded with the MainActivity.
        mGoogleApiClient = new GoogleApiClientBuilder(getContext())
                .addLocationServicesApi()
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.map_fragment, viewGroup, false);

        getChildFragmentManager().beginTransaction().replace(R.id.support_map_fragment, mMapFragment).commit();

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NewSpottedActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch(requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
        }

        updateLocationUI();
    }

    /**
     * Builds the map when Google play service is connected
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(!mMapInitialized){
            mMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Play services connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Timber.d("Play services connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * This callback is triggered when the map is ready to be used.
     * This is where we can add mMarkers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMapInitialized = true;
        mMap = map;

        //Setting up the cluster manager
        mClusterManager = new NearbyClusterManager<>(getContext(), mMap);
        mClusterManager.setOnCameraIdleListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<SpottedClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<SpottedClusterItem> cluster) {
                if(mMap.getCameraPosition().zoom > 19){
                    ArrayList<Parcelable> spotteds = new ArrayList<>();

                    for(SpottedClusterItem item : cluster.getItems()){
                        spotteds.add(item);
                    }

                    Intent intent = new Intent(getActivity(), SpottedClusterDetailActivity.class);
                    intent.putParcelableArrayListExtra(SpottedClusterDetailActivity.EXTRAS_SPOTTEDS, spotteds);
                    getActivity().startActivity(intent);
                }
                else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), mMap.getCameraPosition().zoom + 1));
                }

                return true;
            }
        });

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        //Disable Map Toolbar:
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    @Override
    public void onCameraIdle() {
        mCameraPosition = mMap.getCameraPosition();

        Projection projection = mMap.getProjection();
        VisibleRegion visibleRegion = projection.getVisibleRegion();

        LatLng northeast = visibleRegion.latLngBounds.northeast;
        LatLng southWest = visibleRegion.latLngBounds.southwest;

        mPresenter.getSpotteds(southWest.latitude,northeast.latitude,southWest.longitude,northeast.longitude);
    }

    @Override
    public boolean onClusterItemClick(SpottedClusterItem spottedClusterItem) {
        Intent intent = new Intent(getActivity(), SpottedDetailActivity.class);
        intent.putExtra(SpottedDetailActivity.EXTRAS_SPOTTED_ID,spottedClusterItem.getId());
        getActivity().startActivity(intent);

        return true;
    }

    @Override
    public void onSpottedsReceived(List<Spotted> spotteds) {
        for (Spotted spotted : spotteds) {
            SpottedClusterItem item = new SpottedClusterItem(spotted);
            mClusterManager.addItem(item);
        }

        mClusterManager.cluster();
    }

    private void updateLocationUI() {
        if(mMap == null){
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
        * device. The result of the permission request is handled by a callback,
        * onRequestPermissionsResult.
        */
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    private void getDeviceLocation(){
        if (ContextCompat.checkSelfPermission(getContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        /*
         * Before getting the device location, you must check location
         * permission, as described earlier in the tutorial. Then:
         * Get the best and most recent location of the device, which may be
         * null in rare cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            LatLng currentLatLng = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
        } else {
            Timber.d("Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
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
            Spotted spotted = new Spotted(Integer.toString(i),"Y ce passe quelque chose",lat, lng);
            SpottedClusterItem spottedClusterItem = new SpottedClusterItem(spotted);

            mClusterManager.addItem(spottedClusterItem);
        }
    }
}
