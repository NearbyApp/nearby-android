package io.nearby.android.google.maps;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Marc on 2017-01-31.
 */

public class NearbyClusterManager<T extends ClusterItem> extends ClusterManager<T>{

    private GoogleMap.OnCameraIdleListener mListener;
    private MapIconRenderer<T> mRenderer;
    private List<T> mClusterItems;

    public NearbyClusterManager(Context context, GoogleMap map) {
        super(context, map);

        init(context,map);
    }

    public NearbyClusterManager(Context context, GoogleMap map, MarkerManager markerManager) {
        super(context, map, markerManager);

        init(context,map);
    }

    private void init(Context context, GoogleMap map){
        //Setting a custom renderer to shot unique spotted with custom marker.
        mRenderer = new MapIconRenderer<>(context, map, this);
        this.setRenderer(mRenderer);

        mClusterItems = new ArrayList<>();
    }

    @Override
    public void onCameraIdle() {
        super.onCameraIdle();

        mListener.onCameraIdle();
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

    public void setOnCameraIdleListener(GoogleMap.OnCameraIdleListener listener){
        mListener = listener;
    }

    public void setOnClusterItemClickListener(OnClusterItemClickListener<T> listener){
        mRenderer.setOnClusterItemClickListener(listener);
    }
}
