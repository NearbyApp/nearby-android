package io.nearby.android.ui.map;

import java.util.List;

import io.nearby.android.data.Spotted;
import io.nearby.android.ui.BaseView;

/**
 * Created by Marc on 2017-02-16.
 */

public interface MapContract {

    interface View extends BaseView<Presenter>{
        void onSpottedsReceived(List<Spotted> spotteds);
        void onSpottedDetailReceived(Spotted spotted);
    }

    interface Presenter{
        void getSpotteds(double minLat, double maxLat,
                         double minLng, double maxLng);
        void getSpottedDetails(Spotted spotted);
    }
}
