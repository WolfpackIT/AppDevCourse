package nl.wolfpackit.callemailapp;

import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SendSlackMessage implements Runnable {
    @Override
    public void run() {

        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        if (!availableInternetConnection()) {
            Toast.makeText(getContext(), "Internet connection is unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        final String url = "";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response", response.toString());
                }
            },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error.response", error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //TODO: Add api-specific params here
                //params.put();
                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    private boolean availableInternetConnection() {
        //TODO: Implement method
    }


}
