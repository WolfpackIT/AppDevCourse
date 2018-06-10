package com.example.kath.appdev6kath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ShoutFragment extends Fragment implements ExampleAdapter.OnItemClickListener {
    private static final String TAG = "ShoutFragment";
    public static final String EXTRA_SHOUTS = "creatorName";

    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ShoutItem> mExampleList;
    private RequestQueue mRequestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView : Starting");
        View v = inflater.inflate(R.layout.fragment_shout, container, false);

		// Setup recycler view etc
        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
	
        mExampleList = new ArrayList<>();

		mRecyclerView.setAdapter(new ExampleAdapter(getActivity(),mExampleList));
		
		// Setup http request queue
		mRequestQueue = Volley.newRequestQueue(getContext());
		
		// Parse JSON. I think this for getting
		// some random strings (user names) for
		// the shout list elements.
        parseJSON();

        return v;
    }

    private void parseJSON() {

		// This is the url that 
		// will a JSON object containing
		// among other things, user names.
        String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=kitten&image_type=photo&pretty=true";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hits");
							
							// Add shout elements based on hit user names
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String shoutName = hit.getString("user");
                                mExampleList.add(new ShoutItem(shoutName));
							}
							
							// Initialize adapter with list and attach adapter
                            mExampleAdapter = new ExampleAdapter(getActivity(), mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);
							
							// Listen to shout element list
							// clicks.
                            mExampleAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {

									// Start shout detail intent and
									// and get clicked item.
                                    Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                                    ShoutItem clickedItem = mExampleList.get(position);

									// Add clicked item to intent bundle.
									// Nice that you're using a bundle for this.
									// See extra comments in DetailActivity.
                                    detailIntent.putExtra(EXTRA_SHOUTS, clickedItem.getmShoutName());
                                    startActivity(detailIntent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

	// Not sure why you have 
	// an identical extra onClickListener.
	// I think it is much nicer to have
	// it here as a method rather than in
	// parseJSON, so rather remove that one.
    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        ShoutItem clickedItem = mExampleList.get(position);

        detailIntent.putExtra(EXTRA_SHOUTS, clickedItem.getmShoutName());
        startActivity(detailIntent);
    }
}