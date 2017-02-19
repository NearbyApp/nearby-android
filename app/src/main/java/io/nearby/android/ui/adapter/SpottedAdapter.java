package io.nearby.android.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.List;

import io.nearby.android.R;
import io.nearby.android.data.Spotted;

/**
 * Created by Marc on 2017-02-02.
 */
public class SpottedAdapter extends ArrayAdapter<Spotted> {

    private List<Spotted> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class SpottedViewHolder extends RecyclerView.ViewHolder {
        TextView mMessageTextView;
        ImageView mImageView;

        public SpottedViewHolder(View v) {
            super(v);
            //TODO Call findViewById to set view value

        }
    }

    public SpottedAdapter(Context context) {
        super(context, R.layout.spotted_card);
        mDataset = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Spotted spotted = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spotted_card, parent, false);
        }

        TextView messageTextView = (TextView) convertView.findViewById(R.id.spotted_message);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.spotted_image);

        messageTextView.setText(spotted.getMessage());
        if(imageView.getDrawable() == null){
            Glide.with(getContext()).load(spotted.getPictureUrl()).into(imageView);
        }

        return convertView;
    }

    @Override
    public int getPosition(Spotted item) {
        return mDataset.indexOf(item);
    }

    @Nullable
    @Override
    public Spotted getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public int getCount() {
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

    public Spotted getLastSpotted(){
        Spotted lastSpotted = null;
        if(mDataset.size() > 0){
            lastSpotted = mDataset.get(mDataset.size() - 1 );
        }

        return lastSpotted;
    }
}
