package io.nearby.android.ui.spottedclusterdetail;

import java.util.List;

import javax.inject.Inject;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.ui.BasePresenter;

public class SpottedClusterDetailPresenter implements SpottedClusterDetailContract.Presenter {

    private SpottedClusterDetailContract.View mView;
    private DataManager mDataManager;

    @Inject
    public SpottedClusterDetailPresenter(SpottedClusterDetailContract.View view, DataManager dataManager){
        mView = view;
        mDataManager = dataManager;
    }

    @Override
    public void getSpottedsDetails(double minLat, double maxLat, double minLng, double maxLng) {
        mDataManager.loadSpotted(minLat,
                maxLat,
                minLng,
                maxLng,
                false,
                new SpottedDataSource.SpottedLoadedCallback() {
                    @Override
                    public void onSpottedLoaded(List<Spotted> spotted) {
                        mView.hideProgressBar();
                        mView.onSpottedsReceived(spotted);
                    }

                    @Override
                    public void onError(SpottedDataSource.ErrorType errorType) {
                        mView.hideProgressBar();
                        if(!BasePresenter.manageError(mView, errorType)){
                            mView.spottedLoadingError();
                        }
                    }
                });
    }
}
