package nl.wolfpackit.appdev12.geofence;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.location.GeofencingClient;

import java.text.DecimalFormat;
import java.util.List;

import nl.wolfpackit.appdev12.MainActivity;
import nl.wolfpackit.appdev12.R;

public class FenceListAdapter extends RecyclerView.Adapter<FenceListAdapter.ViewHolder> {
    private List<Fence> fences;
    private GeofencingClient mGeofencingClient;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public FenceListAdapter(List<Fence> fences, Application context) {
        this.fences = fences;
        mGeofencingClient = new GeofencingClient(context);
    }

    public boolean checkLocation(double lat, double lon){
        for(Fence fence: fences){
            if(fence.isInRange(lat, lon))
                return true;
        }
        return false;
    }


    @Override
    public FenceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_fence, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Fence fence = fences.get(position);
        AppCompatTextView latitude = ((AppCompatTextView)holder.view.findViewById(R.id.latitude));
        AppCompatTextView longitude = ((AppCompatTextView)holder.view.findViewById(R.id.longitude));
        AppCompatTextView name = ((AppCompatTextView)holder.view.findViewById(R.id.flagName));
        final ImageButton deleteButton = ((ImageButton)holder.view.findViewById(R.id.deleteFlagButton));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteFence(fence, MainActivity.mPrefs);
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat(".#####");
        latitude.setText(decimalFormat.format(fence.getLatitude()));
        longitude.setText(decimalFormat.format(fence.getLongitude()));
        System.out.println(fence.getLatitude()+" "+fence.getLongitude()+"  "+decimalFormat.format(fence.getLatitude())+"  "+decimalFormat.format(fence.getLongitude()));
        name.setText(fence.getName());
    }

    @Override
    public int getItemCount() {
        return fences.size();
    }

    public void insertFence(Fence fence, SharedPreferences mPrefs){
        fences.add(fence);
        updateFences(mPrefs);
    }
    public void deleteFence(Fence fence, SharedPreferences mPrefs){
        fences.remove(fence);
        updateFences(mPrefs);
    }
    public void updateFences(SharedPreferences mPrefs){
        String outp = "";
        for(Fence fence: fences){
            outp += ","+fence.export();
        }
        mPrefs.edit().putString("fences", outp.substring(Math.min(1, outp.length()))).commit();
        notifyDataSetChanged();
    }
}
