package io.nearby.android.ui.map.cluster;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.nearby.android.R;
import io.nearby.android.util.ImageUtil;

/**
 * Created by Marc on 2017-01-31.
 */

public class NearbyClusterManager<T extends ClusterItem> extends ClusterManager<T>{

    private GoogleMap.OnCameraIdleListener mListener;
    private MapIconRenderer<T> mRenderer;
    private List<T> mClusterItems;

    public NearbyClusterManager(Context context, GoogleMap map) {
        super(context, map);

        //Setting a custom renderer to shot unique spotted with custom marker.
        mRenderer = new MapIconRenderer<>(context, map, this);
        this.setRenderer(mRenderer);

        mClusterItems = new ArrayList<>();
    }

    @Override
    public void onCameraIdle() {
        super.onCameraIdle();

        if(mListener != null){
            mListener.onCameraIdle();
        }

        cluster();
    }

    @Override
    public void addItem(T myItem) {
        if(!mClusterItems.contains(myItem)){
            mClusterItems.add(myItem);
            super.addItem(myItem);
        }
    }

    @Override
    public void addItems(Collection<T> items) {
        for (T item : items) {
            if(!mClusterItems.contains(item)){
                mClusterItems.add(item);
                super.addItem(item);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        super.onMarkerClick(marker);
        return true;
    }

    public void setOnCameraIdleListener(GoogleMap.OnCameraIdleListener listener){
        mListener = listener;
    }

    public void setOnClusterItemClickListener(OnClusterItemClickListener<T> listener){
        mRenderer.setOnClusterItemClickListener(listener);
    }

    public class MapIconRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T> {

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

        @Override
        protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
            return cluster.getItems().size() > 2;
        }
    }
}
