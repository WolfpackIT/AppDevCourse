package com.example.wolfpackapp.DeclarationsFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.wolfpackapp.DeclarationActivity;
import com.example.wolfpackapp.DeclarationDatabase.DecDB;
import com.example.wolfpackapp.DeclarationDatabase.Declaration;
import com.example.wolfpackapp.DeclarationDatabase.DeclarationCar;
import com.example.wolfpackapp.DeclarationDatabase.DeclarationOther;
import com.example.wolfpackapp.DeclarationsFragments.MyDeclaration;
import com.example.wolfpackapp.R;


import java.util.List;
//TODO fix this in a way so that one list contains all declaratiions and their data.


public class FullDeclarationAdapter extends RecyclerView.Adapter<FullDeclarationAdapter.ViewHolder> {

    DecDB db;
    public Declaration shouts;
    public List<DeclarationCar> car;
    public List<DeclarationOther> other;
    private Context mContext;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView shout;
        TextView date;
        TextView money;
        Long uid;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            shout = (TextView) view.findViewById(R.id.FullDecTitle);
            date = (TextView) view.findViewById(R.id.FullDecDate);
            money = (TextView) view.findViewById(R.id.FullDecMoney);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DeclarationActivity.class);
            intent.putExtra("id", uid);
            mContext.startActivity(intent);
        }
    }


    public FullDeclarationAdapter( Declaration shouts,List<DeclarationOther> other, List<DeclarationCar> car, Context ct) {
        this.shouts = shouts;
        this.car = car;
        this.other = other;
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
        if(position >= car.size()) {
            position = position - car.size();
            DeclarationOther x = other.get(position);
            holder.money.setText(Double.toString(x.getTotalCost()));
            holder.uid = x.getOID();
        } else {
            DeclarationCar x = car.get(position);
            holder.money.setText(Double.toString(x.getKilometers()*0.19));
            holder.uid = x.getCID();
        }
        holder.shout.setText(shouts.getTitle());
//        holder.money.setText(Double.toString(shout1.getMoney()));
        holder.date.setText(shouts.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return car.size() + other.size();
    }

//    private class getDeclarations extends AsyncTask<String, Integer, Long> {
//        String TAGB = "getAUTO declarations";
//
//        @Override
//        protected Long doInBackground(String... strings) {
//            car = db.DecDAO().loadAllByCarIds(shouts.getUid());
//            other = db.DecDAO().loadAllByOtherIds(shouts.getUid());
//            return (long) 0;
//        }
//
//        @Override
//        protected void onPostExecute(Long aLong) {
//
//        }
//    }

}