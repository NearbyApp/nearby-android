package io.nearby.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.nearby.android.R;
import io.nearby.android.model.Spotted;

/**
 * Created by Marc on 2017-02-02.
 */
public class SpottedAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private List<Spotted> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class SpottedMessageViewHolder extends RecyclerView.ViewHolder {
        public SpottedMessageViewHolder(View v) {
            super(v);
            //TODO Call findViewById to set view value

        }
    }

    public static class SpottedImageViewHolder extends RecyclerView.ViewHolder {
        public SpottedImageViewHolder(View v) {
            super(v);
            //TODO Call findViewById to set view value
        }
    }

    public SpottedAdapter(){
        mDataset = new ArrayList<>();
    }

    public SpottedAdapter(List<Spotted> dataset){
        mDataset = dataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        switch(viewType){
            case R.layout.spotted_message_card:
                viewHolder = new SpottedMessageViewHolder(view);
                break;
            case R.layout.spotted_image_card:
                viewHolder = new SpottedImageViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Spotted spotted = mDataset.get(position);

        switch (holder.getItemViewType()){
            case R.layout.spotted_message_card:
                SpottedMessageViewHolder spottedMessageViewHolder = (SpottedMessageViewHolder) holder;
                // TODO setValue
                break;
            case R.layout.spotted_image_card:
                SpottedImageViewHolder spottedImageViewHolder = (SpottedImageViewHolder) holder;
                // TODO setValue
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;

        Spotted spotted = mDataset.get(position);
        if(spotted.hasImage()){
            viewType = R.layout.spotted_image_card;
        }
        else {
            viewType = R.layout.spotted_message_card;
        }

        return viewType;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addItem(Spotted spotted){
        mDataset.add(spotted);
        notifyDataSetChanged();
    }

    public void addItems(List<Spotted> spotteds){
        mDataset.addAll(spotteds);
        notifyDataSetChanged();
    }

    public void insert(Spotted spotted){
        mDataset.add(0, spotted);
    }
}
