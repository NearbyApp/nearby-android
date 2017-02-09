package io.nearby.android.ui;

/**
 * Created by Marc on 2017-02-08.
 */

public class Presenter<T extends MvpView> {

    private T mView;

    public void attachView(T view){
        mView = view;
    }

    public void detachView(){
        mView = null;
    }

    public T getView() {
        return mView;
    }
}
