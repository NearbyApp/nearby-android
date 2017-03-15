package io.nearby.android.ui.newspotted;

import java.io.File;

import io.nearby.android.ui.BaseView;

/**
 * Created by Marc on 2017-02-16.
 */

public interface NewSpottedContract {

    interface View extends BaseView<Presenter>{
        void onSpottedCreated();
        void onSpottedNotCreated();
    }

    interface Presenter{
        void createSpotted(double lat, double lng, String message, boolean anonymity, File file);

        boolean getDefaultAnonymity();
        void updateDefaultAnonymity(boolean anonymity);
    }
}
