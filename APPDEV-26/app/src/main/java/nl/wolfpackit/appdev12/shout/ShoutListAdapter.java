package nl.wolfpackit.appdev12.shout;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import nl.wolfpackit.appdev12.MainActivity;
import nl.wolfpackit.appdev12.R;

public class ShoutListAdapter extends RecyclerView.Adapter<ShoutListAdapter.ViewHolder> {
    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public ShoutListAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ShoutListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_shout_button, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

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
            MainActivity.createSimpleNotification(MainActivity.getStringByID(R.string.shoutSuccessNotifcationTitle).replace("[shout]", text),
                                            MainActivity.getStringByID(R.string.shoutSuccessNotifcationBody).replace("[shout]", text));
        }else{
            MainActivity.createSimpleNotification(MainActivity.getStringByID(R.string.shoutFailNotifcationTitle).replace("[shout]", text),
                                            MainActivity.getStringByID(R.string.shoutFailNotifcationBody).replace("[shout]", text));
        }
    }

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
