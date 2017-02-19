package io.nearby.android.ui.myspotted;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    private ListView mListView;
    private SpottedAdapter mAdapter;

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

        mListView = (ListView) view.findViewById(R.id.list_view);

        mAdapter = new SpottedAdapter(getContext());
        mListView.setAdapter(mAdapter);

        // http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
        //TODO
        //mListView.setOnScrollListener();
        /*mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) //check for scroll down
                {
                    int visibleItemCount = mListView.getChildCount();
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
        });*/

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
        mPresenter = (MySpottedPresenter) presenter;
    }
}
