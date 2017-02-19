package io.nearby.android.ui.newspotted;

import android.support.annotation.Nullable;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;

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

    public void createSpotted(double lat, double lng, String message,@Nullable String filePath){
        Spotted spotted = new Spotted("0",message,lat,lng);

        File file = null;

        if(filePath != null){
            file = new File(filePath);
        }

        mDataManager.createSpotted(spotted, file, new SpottedDataSource.SpottedCreatedCallback() {
                    @Override
                    public void onSpottedCreated() {
                        mView.onSpottedCreated();
                    }

                    @Override
                    public void onError() {
                        mView.onSpottedNotCreated();
                    }
                });
    }
}
