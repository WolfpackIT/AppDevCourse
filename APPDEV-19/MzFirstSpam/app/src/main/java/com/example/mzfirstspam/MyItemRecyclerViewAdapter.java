package com.example.mzfirstspam;

import android.content.Context;
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
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

        public String[] shouts;
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
                Snackbar snackbar = Snackbar.make(view, "send: "+shout.getText().toString()+" to general slack ", 1000);
                snackbar.show();

                final String BR = shout.getText().toString();
                String url = "https://hooks.slack.com/services/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";
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("text",BR);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                final String body = jsonObject.toString();

// Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(context);

// Request a string response from the provided URL.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //context.setText("Response: " + response.toString());
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                            }
                        }){
                    @Override
                    public byte[] getBody() {
                        try {
                            return body == null ? null : body.getBytes("utf-8");
                        } catch(UnsupportedEncodingException ex){
                            ex.printStackTrace();
                        }
                        return null;

                    }
                };
                queue.add(jsonObjectRequest);



            }
        }


    public MyItemRecyclerViewAdapter(String[] shouts, Context ct) {
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
        String shout = shouts[position];
        holder.shout.setText(shout);
    }

    @Override
    public int getItemCount() {
        return shouts.length;
    }


}