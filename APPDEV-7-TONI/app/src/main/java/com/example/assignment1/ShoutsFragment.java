package com.example.assignment1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wolfpack on 5/24/2018.
 */

public class ShoutsFragment extends Fragment {

    private static final String TAG = "ShoutsFragment";

    private RecyclerView mRecyclerView;
    //Adapter is the bridge between data and recyclerview
    private ShoutAdapter mAdapter;
    //Align the items in the recyclerview
    private RecyclerView.LayoutManager mLayoutManager;

    final ArrayList<ShoutItem> shoutItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shouts, container, false);


        shoutItems.add(new ShoutItem("I need coffee now!"));
        shoutItems.add(new ShoutItem("Who is hungry on a Tuesday?"));
        shoutItems.add(new ShoutItem("You only live once!"));

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ShoutAdapter(shoutItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ShoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                shoutItems.get(position);
                //Insert the POST Volley for Slack API here
                startPostSlackAPIThread(shoutItems.get(position).getShoutText());
                //Test to see getShoutText is retrieved
                Log.d(TAG, shoutItems.get(position).getShoutText());
            }
        });


        return view;
    }

    /*********Volley POST Slack API***********/
    public void postSlackAPI(final String inputShout) {
        String url = "https://hooks.slack.com/services/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";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    Log.d(TAG, response);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }

                try {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject mock = jsonArray.getJSONObject(i);
                        String contentType = mock.getString("Content-Type");
                        String text = mock.getString("text");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
                Log.d(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("text", inputShout);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(postRequest);
    }


    /****************BACKGROUND THREAD: postStackAPI**********************/
    class PostStackAPIRunnable implements Runnable {
       String mShoutText;

       public PostStackAPIRunnable(String shoutText) {
           this.mShoutText = shoutText;
       }

        @Override
        public void run() {
            postSlackAPI(mShoutText);
        }
    }

    public void startPostSlackAPIThread(String text) {
        PostStackAPIRunnable runnable = new PostStackAPIRunnable(text);
        new Thread(runnable).start();
    }
}
