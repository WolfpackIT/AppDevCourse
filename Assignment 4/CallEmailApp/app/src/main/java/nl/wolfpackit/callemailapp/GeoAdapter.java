package nl.wolfpackit.callemailapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GeoAdapter extends RecyclerView.Adapter<GeoAdapter.ViewHolder> {

    private List<String> locationList;

    @NonNull
    @Override
    public GeoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_geo_view, parent, false);
        GeoAdapter.ViewHolder vh = new GeoAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull GeoAdapter.ViewHolder holder, int position) {
        String location = locationList.get(position);
        holder.mTextView.setText(location);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public GeoAdapter(List dataset) {
        locationList = dataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.viewLocation);
        }
    }

}
