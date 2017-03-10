package io.nearby.android.ui.spottedclusterdetail;

import java.util.List;

import io.nearby.android.data.Spotted;
import io.nearby.android.ui.BaseView;

/**
 * Created by Marc on 2017-03-05.
 */

public interface SpottedClusterDetailContract {

    interface View extends BaseView<Presenter>{
        void onSpottedsReceived(List<Spotted> spotteds);

        void hideProgressBar();
        void spottedLoadingError();
    }

    interface Presenter{
        void getSpottedsDetails(double minLat, double maxLat, double minLng, double maxLng);
    }
}
