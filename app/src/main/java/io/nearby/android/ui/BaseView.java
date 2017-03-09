package io.nearby.android.ui;

public interface BaseView<T> {

    void setPresenter(T presenter);

    void onUserAccountDisabled();
    void onUserUnauthorized();

}
