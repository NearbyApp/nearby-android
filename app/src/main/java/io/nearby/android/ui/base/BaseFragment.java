package io.nearby.android.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import io.nearby.android.injection.component.ApplicationComponent;
import io.nearby.android.injection.component.DaggerApplicationComponent;
import io.nearby.android.injection.module.ApplicationModule;

/**
 * Created by Marc on 2017-02-09.
 */

public class BaseFragment extends Fragment {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getActivity().getApplication()))
                .build();
    }
}
