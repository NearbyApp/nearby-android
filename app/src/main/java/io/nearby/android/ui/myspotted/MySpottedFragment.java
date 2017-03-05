package io.nearby.android.ui.myspotted;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.data.Spotted;
import io.nearby.android.ui.adapter.SpottedAdapter;
import io.nearby.android.ui.spotteddetail.SpottedDetailActivity;
import io.reactivex.functions.Consumer;

public class MySpottedFragment extends Fragment implements MySpottedContract.View, SwipeRefreshLayout.OnRefreshListener{

    private static final int VISIBLE_THRESHOLD = 5;
    private static final int LAYOUT = R.layout.my_spotted_fragment;

    @Inject MySpottedPresenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SpottedAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView mEmptyListTextView;
    private ProgressBar mProgressBar;

    private boolean mIsLoadingOlderSpotted = false;
    private boolean mHasOlderSpotted = true;
    private int mPreviousTotal = 0;

    public static MySpottedFragment newInstance() {

        Bundle args = new Bundle();

        MySpottedFragment fragment = new MySpottedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMySpottedComponent.builder()
                .mySpottedPresenterModule(new MySpottedPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getActivity().getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);

        mEmptyListTextView = (TextView) view.findViewById(R.id.empty);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new SpottedAdapter(Glide.with(this));
        mAdapter.setItemClickListener(new Consumer<Spotted>() {
            @Override
            public void accept(Spotted spotted) throws Exception {
                Intent intent = new Intent(MySpottedFragment.this.getActivity(), SpottedDetailActivity.class);
                intent.putExtra(SpottedDetailActivity.EXTRAS_SPOTTED_ID,spotted.getId());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        // http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0 && mHasOlderSpotted) //check for scroll down
                {
                    int visibleItemCount = MySpottedFragment.this.mRecyclerView.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (mIsLoadingOlderSpotted) {
                        if (totalItemCount > mPreviousTotal) {
                            mIsLoadingOlderSpotted = false;
                            mPreviousTotal = totalItemCount;
                        }
                    }
                    if (!mIsLoadingOlderSpotted &&
                            (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                        mPreviousTotal = totalItemCount;
                        mIsLoadingOlderSpotted = true;

                        mPresenter.loadMyOlderSpotted(mAdapter.getItemCount());
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadMySpotted();
    }

    @Override
    public void onRefresh() {
        Date creationDate = mAdapter.getItem(0).getCreationDate();

        if(creationDate != null){
            mPresenter.refreshMySpotted(creationDate);
        }
    }

    @Override
    public void onMySpottedReceived(List<Spotted> spottedList) {
        if(spottedList.size() == 0 && mAdapter.getItemCount() == 0){
            showEmptyListMessage();
        }
        else {
            hideEmptyListMessage();
            mAdapter.addItems(spottedList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMyOlderSpottedReceived(List<Spotted> spottedList) {
        if(spottedList.size() == 0){
            mHasOlderSpotted = false;
        }

        mAdapter.addItems(spottedList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMyNewerSpottedReceived(List<Spotted> mySpotted) {
        mAdapter.insertAll(mySpotted);
    }

    @Override
    public void stopRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(MySpottedContract.Presenter presenter) {
        mPresenter = (MySpottedPresenter) presenter;
    }

    private void showEmptyListMessage() {
        mEmptyListTextView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyListMessage() {
        mEmptyListTextView.setVisibility(View.GONE);
    }

    private void addDummySpotted(){
        List<Spotted> spotteds = new ArrayList<>();

        for (int i = 0; i < 10 ; i++){
            Spotted spotted = new Spotted(Integer.toString(i),"I spotted the spotted #" + i,0,0);
            spotteds.add(spotted);
        }

        mAdapter.addItems(spotteds);
    }
}
