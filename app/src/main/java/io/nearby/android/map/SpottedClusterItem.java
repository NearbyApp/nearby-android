package io.nearby.android.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import io.nearby.android.model.Spotted;

/**
 * Created by Marc on 2017-01-29.
 */

public class SpottedClusterItem extends Spotted implements ClusterItem {

    private LatLng latLng;

    public SpottedClusterItem(Spotted spotted){
        super(spotted.getMessage());

        latLng = new LatLng(Double.parseDouble(spotted.getLatitude()),
                Double.parseDouble(spotted.getLongitude()));
    }

    public SpottedClusterItem(Spotted spotted, LatLng latLng) {
        super(spotted.getMessage());
        this.latLng = latLng;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }
}
