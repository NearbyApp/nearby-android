package io.nearby.android.ui.myspotted;

import java.util.List;

import io.nearby.android.data.Spotted;
import io.nearby.android.ui.BaseView;

/**
 * Created by Marc on 2017-02-16.
 */

public interface MySpottedContract {

    interface View extends BaseView<Presenter>{
        void onMySpottedReceived(List<Spotted> spottedList);
    }

    interface Presenter{
        void loadMySpotted();
        void refreshMySpotted();
        void loadMyOlderSpotted(Spotted lastSpotted);
    }
}
