package io.nearby.android.ui.newspotted;

import android.support.annotation.Nullable;

import java.io.File;

import javax.inject.Inject;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.ui.BasePresenter;
import io.nearby.android.util.ImageUtil;

public class NewSpottedPresenter implements NewSpottedContract.Presenter{

    private NewSpottedContract.View mView;
    private DataManager mDataManager;

    @Inject
    public NewSpottedPresenter(NewSpottedContract.View view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
    }

    @Inject
    void setupListeners(){
        mView.setPresenter(this);
    }

    @Override
    public void createSpotted(double lat,
                              double lng,
                              String message,
                              boolean anonymity,
                              @Nullable File file){
        final File compressPicture;

        Spotted spotted = new Spotted(Spotted.DEFAULT_ID,
                message,
                lat,
                lng,
                anonymity);

        if(file != null){
            compressPicture = ImageUtil.compressBitmap(file);
        }
        else {
            compressPicture = null;
        }

        mDataManager.createSpotted(spotted,
                compressPicture,
                new SpottedDataSource.SpottedCreatedCallback() {
                    @Override
                    public void onSpottedCreated() {
                        mView.onSpottedCreated();
                        if(compressPicture != null){
                            compressPicture.delete();
                        }
                    }

                    @Override
                    public void onError(SpottedDataSource.ErrorType errorType) {
                        if(!BasePresenter.manageError(mView, errorType)){
                            mView.onSpottedNotCreated();
                        }
                        if(compressPicture != null){
                            compressPicture.delete();
                        }
                    }
                });
    }

    @Override
    public boolean getDefaultAnonymity() {
        return mDataManager.getDefaultAnonymity();
    }

    @Override
    public void updateDefaultAnonymity(boolean anonymity) {
        mDataManager.setDefaultAnonymity(anonymity);
    }
}
