package io.nearby.android.ui.spotteddetail;

import io.nearby.android.data.Spotted;
import io.nearby.android.ui.BaseView;

public interface SpottedDetailContract {

    interface View extends BaseView<Presenter>{
        void onSpottedDetailsReceived(Spotted spotted);
    }

    interface Presenter{
        void loadSpottedDetails(String spottedId);
    }
}
