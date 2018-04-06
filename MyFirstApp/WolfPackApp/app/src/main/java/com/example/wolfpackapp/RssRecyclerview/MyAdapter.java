package com.example.wolfpackapp.RssRecyclerview;

/**
 * Created by Wolfpack on 4/6/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.wolfpackapp.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context c;
    ArrayList<String> headlines;

    public MyAdapter(Context c, ArrayList<String> headlines) {
        this.c = c;
        this.headlines = headlines;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.headlineTxt.setText(headlines.get(position));

    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }
}