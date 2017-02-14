package io.nearby.android.ui;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Marc on 2017-02-14.
 */

public abstract class Presenter {

    protected CompositeDisposable mCompositeDisposable;

    public Presenter(){
        mCompositeDisposable = new CompositeDisposable();
    }

    public void onDestroy(){
        mCompositeDisposable.dispose();
    }
}
