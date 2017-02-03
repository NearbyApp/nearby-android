package io.nearby.android.map;

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

    public NearbyClusterManager(Context context, GoogleMap map) {
        super(context, map);
        this.mMap = map;
    }

    public NearbyClusterManager(Context context, GoogleMap map, MarkerManager markerManager) {
        super(context, map, markerManager);
        this.mMap = map;
    }

    @Override
    public void onCameraIdle() {
        super.onCameraIdle();

        Projection projection = mMap.getProjection();
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
        return super.onMarkerClick(marker);
    }
}
