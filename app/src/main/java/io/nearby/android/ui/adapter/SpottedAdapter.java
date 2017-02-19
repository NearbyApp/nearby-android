package io.nearby.android.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.nearby.android.R;
import io.nearby.android.data.Spotted;

/**
 * Created by Marc on 2017-02-02.
 */
public class SpottedAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private List<Spotted> mDataset;
    private RequestManager mGlide;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class SpottedViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView pictureImageView;

        public SpottedViewHolder(View v) {
            super(v);
            messageTextView = (TextView) v.findViewById(R.id.spotted_message);
            pictureImageView = (ImageView) v.findViewById(R.id.spotted_image);
        }
    }

    public SpottedAdapter(RequestManager glide) {
        mDataset = new ArrayList<>();
        mGlide = glide;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spotted_card, parent, false);
        RecyclerView.ViewHolder viewHolder = new SpottedViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Spotted spotted = mDataset.get(position);
        SpottedViewHolder spottedViewHolder = (SpottedViewHolder) holder;

        spottedViewHolder.messageTextView.setText(spotted.getMessage());

        if (spotted.getPictureUrl() == null) {
            Glide.clear(spottedViewHolder.pictureImageView);
        } else{
            mGlide.load(spotted.getPictureUrl())
                    .into(spottedViewHolder.pictureImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addItem(Spotted spotted) {
        mDataset.add(spotted);
        notifyDataSetChanged();
    }

    public void addItems(List<Spotted> spotteds) {
        mDataset.addAll(spotteds);
        notifyDataSetChanged();
    }

    public void insert(Spotted spotted) {
        mDataset.add(0, spotted);
    }

    public Spotted getLastSpotted(){
        Spotted lastSpotted = null;
        if(mDataset.size() > 0){
            lastSpotted = mDataset.get(mDataset.size() - 1 );
        }

        return lastSpotted;
    }
}