package io.nearby.android.ui.spotteddetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.data.Spotted;
import io.nearby.android.ui.BaseActivity;

public class SpottedDetailActivity extends BaseActivity<SpottedDetailContract.Presenter> implements SpottedDetailContract.View{

    public static final String EXTRAS_SPOTTED_ID = "extras_spotted_id";

    private TextView mMessageTextView;
    private ImageView mSpottedPictureImageView;
    private TextView mFullNameTextView;
    private ImageView mProfilePictureImageView;
    private View mProgressBarContainer;
    private View mErrorMessage;

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSpottedDetailsReceived(Spotted spotted) {
        mMessageTextView.setText(spotted.getMessage());

        if(!spotted.isAnonymous()){
            mFullNameTextView.setText(spotted.getFullName());

            Glide.with(this)
                    .load(spotted.getProfilePictureUrl())
                    .fallback(R.drawable.ic_person)
                    .into(mProfilePictureImageView);
        }

        if(spotted.getPictureUrl() != null){
            Glide.with(this)
                    .load(spotted.getPictureUrl())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            hideProgressBar();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            hideProgressBar();
                            return false;
                        }
                    })
                    .into(mSpottedPictureImageView);
        }
        else {
            hideProgressBar();
        }
    }

    @Override
    public void spottedDetailsLoadingError() {
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void initializeView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBarContainer = findViewById(R.id.progress_bar_container);
        mErrorMessage = findViewById(R.id.error);

        mMessageTextView = (TextView) findViewById(R.id.spotted_message);
        mSpottedPictureImageView = (ImageView) findViewById(R.id.spotted_picture);
        mFullNameTextView = (TextView) findViewById(R.id.spotted_full_name);
        mProfilePictureImageView = (ImageView) findViewById(R.id.spotted_profile_picture);
    }

    @Override
    public void hideProgressBar(){
        mProgressBarContainer.setVisibility(View.GONE);
    }
}
