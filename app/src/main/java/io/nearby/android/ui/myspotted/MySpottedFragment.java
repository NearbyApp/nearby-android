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

import io.nearby.android.R;
import io.nearby.android.ui.adapter.SpottedAdapter;
import io.nearby.android.data.model.Spotted;

/**
 * Created by Marc on 2017-02-02.
 */

public class MySpottedFragment extends Fragment implements MySpottedView, SwipeRefreshLayout.OnRefreshListener{

    private MySpottedPresenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SpottedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static MySpottedFragment newInstance() {

        Bundle args = new Bundle();

        MySpottedFragment fragment = new MySpottedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new MySpottedPresenter();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_spotted_fragment, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SpottedAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.loadMySpotted();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addDummySpotted();
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

        for (int i = 0; i < 25 ; i++){
            Spotted spotted = new Spotted("I spotted the spotted #" + i);
            spotteds.add(spotted);
        }

        mAdapter.addItems(spotteds);
    }
}
