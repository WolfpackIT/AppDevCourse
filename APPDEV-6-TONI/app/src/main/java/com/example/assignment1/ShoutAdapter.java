package com.example.assignment1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Wolfpack on 5/24/2018.
 */

public class ShoutAdapter extends RecyclerView.Adapter<ShoutAdapter.ShoutViewHolder> {
    private ArrayList<ShoutItem> mShoutItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ShoutViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewShout;

        public ShoutViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextViewShout = itemView.findViewById(R.id.shoutTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ShoutAdapter(ArrayList<ShoutItem> shoutItem) {
        this.mShoutItems = shoutItem;
    }

    //CreateViewHolder creates the Items layout
    @Override
    public ShoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_shouts_items, parent, false);
        ShoutViewHolder svh = new ShoutViewHolder(v, mListener);
        return svh;

    }

    //onBindViewHolder passes the values from the ShoutViewHolder
    @Override
    public void onBindViewHolder(ShoutViewHolder holder, int position) {
        ShoutItem currentItem = mShoutItems.get(position);
        holder.mTextViewShout.setText(currentItem.getShoutText());
    }

    @Override
    public int getItemCount() {
        return mShoutItems.size();
    }
}
