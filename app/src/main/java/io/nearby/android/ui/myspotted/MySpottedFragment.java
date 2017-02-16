package io.nearby.android.ui.myspotted;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.data.Spotted;
import io.nearby.android.ui.adapter.SpottedAdapter;

/**
 * Created by Marc on 2017-02-02.
 */

public class MySpottedFragment extends Fragment implements MySpottedContract.View, SwipeRefreshLayout.OnRefreshListener{

    private static final int VISIBLE_THRESHOLD = 5;

    private MySpottedContract.Presenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SpottedAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean mIsLoadingOlderSpotted = false;
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
                .taskDetailPresenterModule(new MySpottedPresenterModule(this))
                .tasksRepositoryComponent(((NearbyApplication) getActivity().getApplication())
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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SpottedAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) //check for scroll down
                {
                    int visibleItemCount = mRecyclerView.getChildCount();
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

                        mPresenter.loadMyOlderSpotted(mAdapter.getLastSpotted());

                        mIsLoadingOlderSpotted = true;
                    }
                }
            }
        });

        mPresenter.loadMySpotted();

        return view;
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshMySpotted();
    }

    @Override
    public void onMySpottedReceived(List<Spotted> spottedList) {
        //TODO A merging would probably be better
        mAdapter.addItems(spottedList);
        mAdapter.notifyDataSetChanged();

        mSwipeRefreshLayout.setRefreshing(false);
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
        mPresenter = presenter;
    }
}
