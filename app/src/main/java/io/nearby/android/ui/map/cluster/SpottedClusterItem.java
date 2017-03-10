package io.nearby.android.ui.map.cluster;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import io.nearby.android.data.Spotted;

/**
 * Created by Marc on 2017-01-29.
 */

public class SpottedClusterItem extends Spotted implements ClusterItem, Parcelable {

    public SpottedClusterItem(Spotted spotted){
        super(spotted.getId(), spotted.getMessage(), spotted.getLatitude(), spotted.getLongitude());
    }

    @Override
    public LatLng getPosition() {
        return getLatLng();
    }
}
