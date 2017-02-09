package io.nearby.android.google.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import io.nearby.android.data.model.Spotted;

/**
 * Created by Marc on 2017-01-29.
 */

public class SpottedClusterItem extends Spotted implements ClusterItem {

    public SpottedClusterItem(Spotted spotted){
        super(spotted.getMessage(), spotted.getLatitude(), spotted.getLongitude());
    }

    @Override
    public LatLng getPosition() {
        return getLatLng();
    }
}
