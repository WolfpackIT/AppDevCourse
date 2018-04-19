package com.example.wolfpackapp.decleratonRecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.wolfpackapp.DeclarationDatabase.Declaration;
import com.example.wolfpackapp.R;


import java.util.List;


public class DeclarationAdapter extends RecyclerView.Adapter<DeclarationAdapter.ViewHolder> {

    public List<Declaration> shouts;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView shout;
        TextView date;
        TextView money;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            shout = (TextView) view.findViewById(R.id.decTitle);
            date = (TextView) view.findViewById(R.id.decDate);
            money = (TextView) view.findViewById(R.id.decMoney);
        }

        @Override
        public void onClick(View view) {


        }
    }


    public DeclarationAdapter(List<Declaration> shouts, Context ct) {
        this.shouts = shouts;
        this.mContext = ct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.declaration_recycler_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Declaration shout = shouts.get(position);
        holder.shout.setText(shout.getTitle());
        holder.money.setText(Double.toString(shout.getMoney()));
        holder.date.setText(shout.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return shouts.size();
    }


}