package io.nearby.android.ui.map;

import java.util.List;

import io.nearby.android.data.Spotted;

/**
 * Created by Marc on 2017-02-12.
 */

public interface MapView {
    void onSpottedsReceived(List<Spotted> spotteds);
    void onSpottedDetailReceived(Spotted spotted);
}
