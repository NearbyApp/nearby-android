package io.nearby.android.ui.myspotted;

import java.util.List;

import io.nearby.android.data.model.Spotted;
import io.nearby.android.ui.MvpView;

/**
 * Created by Marc on 2017-02-08.
 */

public interface MySpottedView extends MvpView {

    void onMySpottedReceived(List<Spotted> spottedList);

}
