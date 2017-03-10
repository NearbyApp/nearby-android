package io.nearby.android.ui.myspotted;

import java.util.Date;
import java.util.List;

import io.nearby.android.data.Spotted;
import io.nearby.android.ui.BaseView;

/**
 * Created by Marc on 2017-02-16.
 */

public interface MySpottedContract {

    interface View extends BaseView<Presenter>{
        void onMySpottedReceived(List<Spotted> spottedList);
        void onMyOlderSpottedReceived(List<Spotted> spottedList);
        void onMyNewerSpottedReceived(List<Spotted> mySpotted);

        void loadingMySpottedFailed();

        void stopRefreshing();
        void hideLoadingProgressBar();

        void loadOlderFailed();

        void refreshFailed();
    }

    interface Presenter{
        void loadMySpotted();
        void refreshMySpotted(Date mostRecentSpotted);
        void loadMyOlderSpotted(int spottedCount);
    }
}
