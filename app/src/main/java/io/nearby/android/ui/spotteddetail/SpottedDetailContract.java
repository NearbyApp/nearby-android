package io.nearby.android.ui.spotteddetail;

import io.nearby.android.data.Spotted;
import io.nearby.android.ui.BaseView;

public interface SpottedDetailContract {

    public interface View extends BaseView<Presenter>{
        void onSpottedDetailsReceived(Spotted spotted);
    }

    public interface Presenter{
        void loadSpottedDetails(String spottedId);
    }
}
