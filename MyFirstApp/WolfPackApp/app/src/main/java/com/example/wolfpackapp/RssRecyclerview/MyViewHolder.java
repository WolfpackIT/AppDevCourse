package com.example.wolfpackapp.RssRecyclerview;

/**
 * Created by Wolfpack on 4/6/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wolfpackapp.R;


public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView headlineTxt;

    public MyViewHolder(View itemView) {
        super(itemView);

        headlineTxt= (TextView) itemView.findViewById(R.id.headlineTxt);
    }
}