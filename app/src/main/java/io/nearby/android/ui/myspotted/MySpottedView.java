package io.nearby.android.ui.myspotted;

import java.util.List;

import io.nearby.android.data.Spotted;

/**
 * Created by Marc on 2017-02-08.
 */

public interface MySpottedView {

    void onMySpottedReceived(List<Spotted> spottedList);

}
