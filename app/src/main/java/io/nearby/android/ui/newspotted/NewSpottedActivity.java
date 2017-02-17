package io.nearby.android.ui.newspotted;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.google.GoogleApiClientBuilder;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Marc on 2017-02-02.
 */

public class NewSpottedActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, NewSpottedContract.View, GoogleApiClient.ConnectionCallbacks {


    private Toolbar mToolbar;
    private EditText mEditText;
    private ImageButton mSendButton;

    @Inject NewSpottedPresenter mPresenter;

    private boolean mGoogleLocationServiceIsConnected = false;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_spotted_activity);

        findViewById(R.id.upload_picture_button).setOnClickListener(this);
        mSendButton = (ImageButton) findViewById(R.id.send_button);
        mSendButton.setOnClickListener(this);
        mSendButton.setClickable(false);
        mSendButton.setEnabled(false);

        mEditText = (EditText) findViewById(R.id.spotted_text);
        mEditText.addTextChangedListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGoogleApiClient = GoogleApiClientBuilder.buildLocationApiclient(this, this, null);

        DaggerNewSpottedComponent.builder()
                .newSpottedPresenterModule(new NewSpottedPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                String text = mEditText.getText().toString();

                if (mGoogleLocationServiceIsConnected) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider validating that we
                        return;
                    }

                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    double lat = lastLocation.getLatitude();
                    double lng = lastLocation.getLongitude();

                    mPresenter.createSpotted(lat,lng,text);
                }
                break;
            case R.id.upload_picture_button:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

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

    @Override
    public void onSpottedCreated() {
        onBackPressed();
    }

    @Override
    public void onSpottedNotCreated(){
        Toast.makeText(this,R.string.new_spotted_not_created,Toast.LENGTH_LONG).show();
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
}
