package io.nearby.android.ui.myspotted;

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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.data.Spotted;
import io.nearby.android.ui.adapter.SpottedAdapter;


/**
 * Created by Marc on 2017-02-02.
 */

public class MySpottedFragment extends Fragment implements MySpottedContract.View, SwipeRefreshLayout.OnRefreshListener{

    private static final int VISIBLE_THRESHOLD = 5;

    @Inject MySpottedPresenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SpottedAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean mIsLoadingOlderSpotted = false;
    private boolean mHasOlderSpotted = false;
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
        View view = inflater.inflate(R.layout.my_spotted_fragment, container, false);

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
        //TODO refresh
        //mPresenter.refreshMySpotted();
        //TODO remove this hack
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onMySpottedReceived(List<Spotted> spottedList) {
        //TODO A merging would probably be better
        mAdapter.addItems(spottedList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMyOlderSpottedReceived(List<Spotted> spottedList) {
        if(spottedList.size() == 0){
            mHasOlderSpotted = false;
        }

        mAdapter.addItems(spottedList);
        mAdapter.notifyDataSetChanged();
    }

    private void addDummySpotted(){
        List<Spotted> spotteds = new ArrayList<>();

        for (int i = 0; i < 10 ; i++){
            Spotted spotted = new Spotted("I spotted the spotted #" + i,0,0);
            spotteds.add(spotted);
        }

        mAdapter.addItems(spotteds);
    }

    @Override
    public void setPresenter(MySpottedContract.Presenter presenter) {
        mPresenter = (MySpottedPresenter) presenter;
    }
}
