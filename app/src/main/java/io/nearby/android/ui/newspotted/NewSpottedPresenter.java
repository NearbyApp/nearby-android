package io.nearby.android.ui.newspotted;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.graphics.BitmapCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.nearby.android.data.Spotted;
import io.nearby.android.data.source.DataManager;
import io.nearby.android.data.source.SpottedDataSource;
import io.nearby.android.ui.BasePresenter;

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
                              @Nullable String filePath){
        Spotted spotted = new Spotted(Spotted.DEFAULT_ID,
                message,
                lat,
                lng,
                anonymity);

        File file = null;

        if(filePath != null){
            file = new File(filePath);

            /*BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap,1440,1440,false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,75,stream);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(stream.toByteArray()));
            */
        }

        mDataManager.createSpotted(spotted,
                file,
                new SpottedDataSource.SpottedCreatedCallback() {
                    @Override
                    public void onSpottedCreated() {
                        mView.onSpottedCreated();
                    }

                    @Override
                    public void onError(SpottedDataSource.ErrorType errorType) {
                        if(!BasePresenter.manageError(mView, errorType)){
                            mView.onSpottedNotCreated();
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
