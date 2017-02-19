package io.nearby.android.google.maps;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by Marc on 2017-01-31.
 */

public class NearbyClusterManager<SpottedClusterItem extends ClusterItem> extends ClusterManager<SpottedClusterItem> {

    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener mListener;

    public NearbyClusterManager(Context context, GoogleMap map) {
        super(context, map);
        this.mMap = map;

        init(context,map);
    }

    public NearbyClusterManager(Context context, GoogleMap map, MarkerManager markerManager) {
        super(context, map, markerManager);
        this.mMap = map;

        init(context,map);
    }

    private void init(Context context, GoogleMap map){
        //Setting a custom renderer to shot unique spotted with custom marker.
        MapIconRenderer<SpottedClusterItem> renderer = new MapIconRenderer<>(context, map, this);
        this.setRenderer(renderer);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //TODO show the clicked spotted
        return true;
    }

    @Override
    public void onCameraIdle() {
        super.onCameraIdle();

        mListener.onCameraIdle();
    }

    public void setOnCameraIdleListener(GoogleMap.OnCameraIdleListener listener){
        mListener = listener;
    }
}
