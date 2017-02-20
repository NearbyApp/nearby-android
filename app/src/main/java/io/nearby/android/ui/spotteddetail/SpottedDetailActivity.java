package io.nearby.android.ui.spotteddetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.data.Spotted;

public class SpottedDetailActivity extends AppCompatActivity implements SpottedDetailContract.View{

    public static final String EXTRAS_SPOTTED_ID = "extras_spotted_id";

    private TextView mMessageTextView;
    private ImageView mPictureImageView;
    private TextView mFullNameTextView;
    private ImageView mProfilePictureImageView;

    @Inject
    SpottedDetailPresenter mPresenter;

    @Override
    public void setPresenter(SpottedDetailContract.Presenter presenter) {
        mPresenter = (SpottedDetailPresenter) presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerSpottedDetailComponent.builder()
                .spottedDetailPresenterModule(new SpottedDetailPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);

        setContentView(R.layout.spotted_detail_activity);

        initializeView();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String spottedId = (String) extras.get(EXTRAS_SPOTTED_ID);

            if(spottedId != null && !spottedId.isEmpty()){
                mPresenter.loadSpottedDetails(spottedId);
            }
        }
    }

    @Override
    public void onSpottedDetailsReceived(Spotted spotted) {
        mMessageTextView.setText(spotted.getMessage());

        if(spotted.getPictureUrl() != null){
            Glide.with(this).load(spotted.getPictureUrl()).into(mPictureImageView);
        }

        if(spotted.getUserId() != null){
            mFullNameTextView.setText(spotted.getFullName());
            Glide.with(this).load(spotted.getProfilePictureUrl()).into(mProfilePictureImageView);
        }
    }

    private void initializeView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMessageTextView = (TextView) findViewById(R.id.spotted_message);
        mPictureImageView = (ImageView) findViewById(R.id.spotted_picture);
        mFullNameTextView = (TextView) findViewById(R.id.spotted_full_name);
        mProfilePictureImageView = (ImageView) findViewById(R.id.spotted_profile_picture);
    }
}
