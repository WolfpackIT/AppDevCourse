package com.example.mzfirstspam;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.mzfirstspam.dummy.DummyContent.DummyItem;

import org.w3c.dom.Text;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

        public String[] shouts;

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView shout;

            public ViewHolder(View view) {
                super(view);
                shout = (TextView) view.findViewById(R.id.content);

            }
    }


    public MyItemRecyclerViewAdapter(String[] shouts) {
        this.shouts = shouts;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String shout = shouts[position];
        holder.shout.setText(shout);
    }

    @Override
    public int getItemCount() {
        return shouts.length;
    }
}