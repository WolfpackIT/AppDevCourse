package nl.wolfpackit.appdev12;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_shout_button, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Button button = ((Button)holder.view.findViewById(R.id.shoutButton));
        final String buttonText = mDataset[position];
        button.setText(buttonText);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new ShoutTask().execute(buttonText);
                System.out.println("detect "+buttonText);
            }
        });
    }

    public void onShoutReturn(boolean success, String text){
        if(success){ //success
            MainActivity.createNotification(MainActivity.getStringByID(R.string.shoutSuccessNotifcationTitle).replace("[shout]", text),
                                            MainActivity.getStringByID(R.string.shoutSuccessNotifcationBody).replace("[shout]", text));
        }else{
            MainActivity.createNotification(MainActivity.getStringByID(R.string.shoutFailNotifcationTitle).replace("[shout]", text),
                                            MainActivity.getStringByID(R.string.shoutFailNotifcationBody).replace("[shout]", text));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }


    class ShoutTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... params) {
            String text = params[0];
            String payload = "{\"text\": \"" + text + "\"}";
            String requestURL = "https://hooks.slack.com/services/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";

            URL url;
            String response = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(payload);

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                onShoutReturn(true, text);
            } catch (Exception e) {
                e.printStackTrace();
                onShoutReturn(false, text);
            }
            return null;
        }
    }
}
