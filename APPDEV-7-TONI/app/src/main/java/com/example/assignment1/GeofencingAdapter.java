package com.example.assignment1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Wolfpack on 6/4/2018.
 */

public class GeofencingAdapter extends RecyclerView.Adapter<GeofencingAdapter.GeofencingViewHolder> {

    private ArrayList<GeofencingItem> mGeofencingItems;

    public static  class GeofencingViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextLong;
        public TextView mTextLau;

        public GeofencingViewHolder(View itemView) {
            super(itemView);
            mTextLong = itemView.findViewById(R.id.long_text_view);
            mTextLau = itemView.findViewById(R.id.lauti_text_view);
        }
    }

    public GeofencingAdapter(ArrayList<GeofencingItem> geofencingItems) {
        mGeofencingItems = geofencingItems;
    }
    @Override
    public GeofencingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_geofencing_items, parent, false);
        GeofencingViewHolder gvh = new GeofencingViewHolder(v);

        return gvh;
    }

    @Override
    public void onBindViewHolder(GeofencingViewHolder holder, int position) {
        GeofencingItem currentItem = mGeofencingItems.get(position);
        holder.mTextLau.setText("Longitude: " + currentItem.getLongtitude());
        holder.mTextLong.setText("Latitude: " + currentItem.getLautitde());
    }

    @Override
    public int getItemCount() {
        return mGeofencingItems.size();
    }
}
