package io.nearby.android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import io.nearby.android.R;
import io.nearby.android.data.Spotted;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class SpottedAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private List<Spotted> mDataset;
    private RequestManager mGlide;

    private final PublishSubject<Spotted> onClickSubject = PublishSubject.create();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class SpottedViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView pictureImageView;

        public SpottedViewHolder(View v) {
            super(v);
            messageTextView = (TextView) v.findViewById(R.id.spotted_message);
            pictureImageView = (ImageView) v.findViewById(R.id.spotted_picture);
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
        final Spotted spotted = mDataset.get(position);
        SpottedViewHolder spottedViewHolder = (SpottedViewHolder) holder;

        spottedViewHolder.messageTextView.setText(spotted.getMessage());

        if (spotted.getPictureThumbnailURL() == null) {
            Glide.clear(spottedViewHolder.pictureImageView);
            spottedViewHolder.pictureImageView.setVisibility(View.GONE);
        }
        else{
            spottedViewHolder.pictureImageView.setVisibility(View.VISIBLE);
            mGlide.load(spotted.getPictureThumbnailURL())
                    .into(spottedViewHolder.pictureImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(spotted);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Spotted getItem(int position){
        Spotted spotted = null;
        if(position >= 0 && position < mDataset.size()){
            spotted = mDataset.get(position);
        }
        return spotted;
    }

    public void addItem(Spotted spotted) {
        if(!mDataset.contains(spotted)){
            mDataset.add(spotted);
            notifyDataSetChanged();
        }
    }

    public void addItems(List<Spotted> spotteds) {
        for (Spotted spotted : spotteds){
            if(!mDataset.contains(spotted)) {
                mDataset.add(spotted);
            }
        }
        notifyDataSetChanged();
    }

    public void setItemClickListener(Consumer<Spotted> onNext){
        onClickSubject.subscribe(onNext);
    }

    public void insert(Spotted spotted) {
        if(!mDataset.contains(spotted)){
            mDataset.add(0, spotted);
            notifyDataSetChanged();
        }
    }

    public void insertAll(List<Spotted> spotteds){
        for (int i = spotteds.size()-1; i > 0; i--) {
            Spotted spotted = spotteds.get(i);
            if(!mDataset.contains(spotted)){
                mDataset.add(0, spotted);
            }
        }

        notifyDataSetChanged();
    }
}