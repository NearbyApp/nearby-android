package io.nearby.android.ui.newspotted;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.ui.BaseActivity;
import io.nearby.android.util.GoogleApiClientBuilder;
import io.nearby.android.util.ImageUtil;
import timber.log.Timber;

import static dagger.internal.Preconditions.checkNotNull;

public class NewSpottedActivity extends BaseActivity<NewSpottedContract.Presenter> implements View.OnClickListener, NewSpottedContract.View, GoogleApiClient.ConnectionCallbacks {

    private static final int REQUEST_IMAGE_CAPTURE = 9003;
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 9009;

    private EditText mSpottedMessageEditText;
    private ImageView mSpottedPictureImageView;
    private ImageButton mSpottedAnonymityButton;
    private ImageButton mSendButton;
    private ImageButton mRemovePictureButton;
    private ImageButton mUploadImageButton;

    private ProgressDialog mProgressDialog;

    @Inject NewSpottedPresenter mPresenter;

    private boolean mGoogleLocationServiceIsConnected = false;
    private GoogleApiClient mGoogleApiClient;
    private File mCurrentPhotoFile;
    private boolean mAnonymity;
    private boolean mHasGivenStoragePermission = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_spotted_activity);

        initializeView();

        mGoogleApiClient = new GoogleApiClientBuilder(this)
                .enableAutoManage(this,null)
                .addLocationServicesApi()
                .addConnectionCallbacks(this)
                .build();

        DaggerNewSpottedComponent.builder()
                .newSpottedPresenterModule(new NewSpottedPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);

        mAnonymity = mPresenter.getDefaultAnonymity();
        updateAnonymityIcon();

        checkStoragePermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = ImageUtil.createBitmapFromFile(mCurrentPhotoFile);
                    mSpottedPictureImageView.setImageBitmap(bitmap);
                    mRemovePictureButton.setVisibility(View.VISIBLE);
                }
                else{
                    mCurrentPhotoFile.delete();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(permissions.length > 0 && permissions[0].equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE )){
                        mUploadImageButton.setEnabled(true);
                        mHasGivenStoragePermission = true;
                    }
                }
                else {
                    mUploadImageButton.setEnabled(false);
                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                onSendSpottedButtonClicked();
                break;
            case R.id.upload_picture_button:
                onUploadPictureButtonClicked();
                break;
            case R.id.anonymity_button:
                onAnonymityButtonClicked();
                break;
            case R.id.remove_picture_button:
                mCurrentPhotoFile.delete();
                mCurrentPhotoFile = null;
                mSpottedPictureImageView.setImageDrawable(null);
                mRemovePictureButton.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onSpottedCreated() {
        //Go back to the MainActivity/MapFragment
        onBackPressed();
    }

    @Override
    public void onSpottedNotCreated(){
        Toast.makeText(this,R.string.new_spotted_not_created,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSendingProgressDialog() {
        if(mProgressDialog == null){
            mProgressDialog =  ProgressDialog.show(this, null, "Sending", true, false);
        }
        else {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideSendingProgressDialog() {
        mProgressDialog.hide();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mGoogleLocationServiceIsConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleLocationServiceIsConnected = false;
    }

    @Override
    public void setPresenter(@NonNull NewSpottedContract.Presenter presenter) {
        mPresenter = (NewSpottedPresenter) checkNotNull(presenter);
    }

    private void initializeView(){
        mSendButton = (ImageButton) findViewById(R.id.send_button);
        mRemovePictureButton = (ImageButton) findViewById(R.id.remove_picture_button);
        mSpottedPictureImageView = (ImageView) findViewById(R.id.spotted_picture);
        mSpottedAnonymityButton = (ImageButton) findViewById(R.id.anonymity_button);
        mUploadImageButton = (ImageButton) findViewById(R.id.upload_picture_button);


        mSendButton.setOnClickListener(this);
        mSendButton.setClickable(false);
        mSendButton.setEnabled(false);

        mUploadImageButton.setOnClickListener(this);
        mRemovePictureButton.setOnClickListener(this);
        mSpottedAnonymityButton.setOnClickListener(this);

        mSpottedMessageEditText = (EditText) findViewById(R.id.spotted_message);
        mSpottedMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    mSendButton.setClickable(true);
                    mSendButton.setEnabled(true);
                }
                else {
                    mSendButton.setClickable(false);
                    mSendButton.setEnabled(false);
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void onSendSpottedButtonClicked(){
        String text = mSpottedMessageEditText.getText().toString();

        if (mGoogleLocationServiceIsConnected) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider validating that we
                return;
            }

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(lastLocation == null) {
                new AlertDialog.Builder(this)
                        .setTitle("Turn on localisation")
                        .setMessage("Please turn on localisation to create a new spotted.")
                        .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show();
            }
            else {
                double lat = lastLocation.getLatitude();
                double lng = lastLocation.getLongitude();

                mPresenter.createSpotted(lat,
                        lng,
                        text,
                        mAnonymity,
                        mCurrentPhotoFile);
            }

        }
    }

    private void onUploadPictureButtonClicked(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // If the user didn't give use the write permission we prompt him to give it.
        if(!mHasGivenStoragePermission){
            checkStoragePermission();
        }
        else if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;

            try {
                photoFile = ImageUtil.createImageFile(this);
            } catch (IOException e) {
                Timber.e(e);
            }

            if(photoFile != null){
                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoFile = photoFile;

                Uri photoUri = FileProvider.getUriForFile(this, "io.nearby.android.fileprovider",photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void onAnonymityButtonClicked() {
        boolean tempAnonymity = !mAnonymity;

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAnonymity = !mAnonymity;
                        mPresenter.updateDefaultAnonymity(mAnonymity);
                        updateAnonymityIcon();
                    }
                }).setNegativeButton(R.string.cancel, null);

        if (tempAnonymity) {
            builder.setMessage(R.string.new_spotted_anonymity_dialog_anonymous)
                    .setTitle("Anonymous");
        } else {
            builder.setMessage(R.string.new_spotted_anonymity_dialog_public)
                    .setTitle("Public");
        }

        builder.create().show();
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mHasGivenStoragePermission = true;
            mUploadImageButton.setEnabled(true);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void updateAnonymityIcon(){
        if(mAnonymity){
            mSpottedAnonymityButton.setImageResource(R.drawable.ic_visibility_off);
        }
        else {
            mSpottedAnonymityButton.setImageResource(R.drawable.ic_visibility);
        }
    }
}
