package io.nearby.android.ui.spotteddetail;

import javax.inject.Inject;

import io.nearby.android.data.source.DataManager;

public class SpottedDetailPresenter implements SpottedDetailContract.Presenter {

    private SpottedDetailContract.View mView;
    private DataManager mDataManager;

    @Inject
    public SpottedDetailPresenter(SpottedDetailContract.View view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
    }

    @Inject
    void setupListeners(){
        mView.setPresenter(this);
    }
}
