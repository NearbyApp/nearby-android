package io.nearby.android.ui.spottedclusterdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.data.Spotted;
import io.nearby.android.ui.BaseActivity;
import io.nearby.android.ui.adapter.SpottedAdapter;
import io.nearby.android.ui.spotteddetail.SpottedDetailActivity;
import io.reactivex.functions.Consumer;

public class SpottedClusterDetailActivity extends BaseActivity<SpottedClusterDetailContract.Presenter> implements SpottedClusterDetailContract.View{

    public static final String EXTRAS_SPOTTEDS = "extras_spotteds";

    private RecyclerView mRecyclerView;
    private SpottedAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView mEmptyListTextView;
    private View mProgressBar;
    private View mErrorMessage;

    @Inject
    SpottedClusterDetailPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerSpottedClusterDetailComponent.builder()
                .spottedClusterDetailPresenterModule(new SpottedClusterDetailPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);

        setContentView(R.layout.spotted_cluster_detail_activity);

        initializeView();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            ArrayList<Spotted> spotteds = extras.getParcelableArrayList(EXTRAS_SPOTTEDS);
            if(spotteds != null && spotteds.size() > 0){

                double minLat = spotteds.get(0).getLatitude(),
                        maxLat = minLat,
                        minLng = spotteds.get(0).getLongitude(),
                        maxLng = minLng;


                for (int i=1 ; i< spotteds.size() ; i++) {
                    minLat = Math.min(spotteds.get(i).getLatitude(), minLat);
                    minLng = Math.min(spotteds.get(i).getLongitude(), minLng);

                    maxLat = Math.max(spotteds.get(i).getLatitude(), maxLat);
                    maxLng = Math.max(spotteds.get(i).getLongitude(), maxLng);
                }

                mPresenter.getSpottedsDetails(minLat, maxLat, minLng, maxLng);
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setPresenter(SpottedClusterDetailContract.Presenter presenter) {
        mPresenter = (SpottedClusterDetailPresenter) presenter;
    }

    @Override
    public void onSpottedsReceived(List<Spotted> spotteds) {
        if(spotteds.size() == 0 && mAdapter.getItemCount() == 0){
            showEmptyListMessage();
        }
        else {
            hideEmptyListMessage();
            mAdapter.addItems(spotteds);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void spottedLoadingError() {
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void initializeView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmptyListTextView = (TextView) findViewById(R.id.empty);
        mProgressBar = findViewById(R.id.progress_bar_container);
        mErrorMessage = findViewById(R.id.error);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new SpottedAdapter(Glide.with(this));
        mAdapter.setItemClickListener(new Consumer<Spotted>() {
            @Override
            public void accept(Spotted spotted) throws Exception {
                Intent intent = new Intent(SpottedClusterDetailActivity.this, SpottedDetailActivity.class);
                intent.putExtra(SpottedDetailActivity.EXTRAS_SPOTTED_ID,spotted.getId());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    private void showEmptyListMessage() {
        mEmptyListTextView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyListMessage() {
        mEmptyListTextView.setVisibility(View.GONE);
    }

}
