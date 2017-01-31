package io.nearby.android.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import io.nearby.android.R;
import io.nearby.android.util.BitmapUtil;

/**
 * Created by Marc on 2017-01-29.
 */

public class MapIconRenderer<SpottedClusterItem extends ClusterItem> extends DefaultClusterRenderer<SpottedClusterItem>  {

    private Context mContext;

    public MapIconRenderer(Context context, GoogleMap map, ClusterManager<SpottedClusterItem> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(SpottedClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.vectorDrawableToBitmap(mContext, R.drawable.circle)));
    }
}
