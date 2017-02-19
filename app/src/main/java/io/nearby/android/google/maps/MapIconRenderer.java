package io.nearby.android.google.maps;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import io.nearby.android.R;
import io.nearby.android.util.ImageUtil;


public class MapIconRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T>  {

    private Context mContext;

    public MapIconRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(T item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.vectorDrawableToBitmap(mContext, R.drawable.circle)));
    }
}
