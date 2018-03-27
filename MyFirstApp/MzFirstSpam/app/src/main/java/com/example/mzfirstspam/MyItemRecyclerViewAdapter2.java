package com.example.mzfirstspam;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mzfirstspam.dummy.DummyContent.DummyItem;
import com.google.android.gms.location.Geofence;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter2 extends RecyclerView.Adapter<MyItemRecyclerViewAdapter2.ViewHolder> {

    public Location[] shouts;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView shout;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            shout = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public void onClick(View view) {
            //TODO
            Snackbar snackbar = Snackbar.make(view, "Not implemented in current version ", 1000);
            snackbar.show();
        }
    }


    public MyItemRecyclerViewAdapter2(Location[] shouts, Context ct) {
        this.shouts = shouts;
        this.context = ct;
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
        Location shout = shouts[position];
        holder.shout.setText( shout.toString());
    }

    @Override
    public int getItemCount() {
        return shouts.length;
    }


}