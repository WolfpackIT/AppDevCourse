package nl.wolfpackit.callemailapp;

import android.content.Context;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class SlackMessageSender implements Runnable {

    private String shout;
    private Context context;
    public boolean shoutPosted = false;

    final String url = "https://hooks.slack.com/services/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";

    public SlackMessageSender(Context context, String shout) {
        this.shout = shout;
        this.context = context;
    }

    @Override
    public void run() {
        Log.d("SlackMessageSender", "inside slack message sender's run");
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        try {
            if (!availableInternetConnection()) {
                Toast.makeText(context, "Internet connection is unavailable", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject jsonBodyObject = new JSONObject();

        try {
            jsonBodyObject.put("text", shout);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String reqBody = jsonBodyObject.toString();
        Log.d("requestBody", reqBody);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", String.valueOf(response));
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
            public byte[] getBody() {
                try{
                    if (reqBody == null){
                        shoutPosted = false;
                        return null;
                    }
                    else {
                        reqBody.getBytes("UTF-8");
                        shoutPosted = true;
                    }
                } catch (UnsupportedEncodingException e) {
                    shoutPosted = false;
                    Log.e(TAG, "Unable to get bytes from JSON", e.fillInStackTrace());
                    return null;
                }
                return null;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }

        };
        requestQueue.add(postRequest);
    }

    private boolean availableInternetConnection() throws IOException, InterruptedException {
        String pingCommand = "ping -c 1 www.google.com";
        return (Runtime.getRuntime().exec(pingCommand).waitFor() == 0);
    }

}
