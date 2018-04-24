package com.example.wolfpackapp.DeclarationsFragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.wolfpackapp.DeclarationDatabase.Declaration;
import com.example.wolfpackapp.DeclarationDatabase.DeclarationCar;
import com.example.wolfpackapp.DeclarationDatabase.DeclarationOther;
import com.example.wolfpackapp.DeclarationsFragments.MyDeclaration;
import com.example.wolfpackapp.R;


import java.util.List;
//TODO fix this in a way so that one list contains all declaratiions and their data. 


public class FullDeclarationAdapter extends RecyclerView.Adapter<FullDeclarationAdapter.ViewHolder> {

    public List<Object> shouts;
    private Context mContext;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView shout;
        TextView date;
        TextView money;
        Long uid;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            shout = (TextView) view.findViewById(R.id.decTitle);
            date = (TextView) view.findViewById(R.id.decDate);
            money = (TextView) view.findViewById(R.id.decMoney);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, MyDeclaration.class);
            intent.putExtra("id", uid);
            mContext.startActivity(intent);
        }
    }


    public FullDeclarationAdapter(List<Object> shouts, Context ct) {
        this.shouts = shouts;
        this.mContext = ct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fulldeclarationlist, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object shout1 = shouts.get(position);
        DeclarationCar x = shout1;
        DeclarationOther y;
        holder.shout.setText(shout1.getTitle());
//        holder.money.setText(Double.toString(shout1.getMoney()));
        holder.date.setText(shout1.getTimestamp());
        holder.uid = shout1.getUid();
        holder.money.setText(Double.toString(shout1.getCash()));
    }

    @Override
    public int getItemCount() {
        return shouts.size();
    }


}