package nl.wolfpack.emailwolfpack.slack;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import nl.wolfpack.emailwolfpack.R;

public class ShoutFragment extends Fragment {
    List<String> shouts;
    RecyclerView recyclerView;
    ShoutsAdapter shoutsAdapter;

    final String SLACK_URL = "https://hooks.slack.com/services/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";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shouts = new ArrayList<>();
    }

    private JsonObjectRequest sendSlackMessage(final String shout) {
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("text", shout);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                SLACK_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.getMessage());
                    }
                }) {
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        return request;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shout_layout, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewShouts);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, final int position) {
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        JSONObject jsonBodyObj = new JSONObject();
                        String url = "https://hooks.slack.com/services/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";
                        try{
                            jsonBodyObj.put("text", shouts.get(position));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        final String requestBody = jsonBodyObj.toString();

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                                url, null, new Response.Listener<JSONObject>(){
                            @Override    public void onResponse(JSONObject response) {
                                Log.i("Response",String.valueOf(response));
                            }
                        }, new Response.ErrorListener() {
                            @Override    public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Error: ", error.getMessage());
                            }
                        }){
                            @Override
                            public byte[] getBody() {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                            requestBody, "utf-8");
                                    return null;
                                }
                            }


                        };

                        queue.add(jsonObjectRequest);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                })
        );
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        recyclerView.addItemDecoration(dividerItemDecoration);

        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");
        shouts.add("lekker eddy");
        shouts.add("hue");

        shoutsAdapter = new ShoutsAdapter(shouts);
        recyclerView.setAdapter(shoutsAdapter);

        return view;
    }



}
