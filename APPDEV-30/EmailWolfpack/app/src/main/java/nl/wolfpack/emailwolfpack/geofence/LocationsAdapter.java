package nl.wolfpack.emailwolfpack.geofence;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.wolfpack.emailwolfpack.R;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.ViewHolder> {
    private List<String> locations;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView location;

        ViewHolder(View view) {
            super(view);
            location = (TextView) view.findViewById(R.id.textViewLocation);
        }
    }

    LocationsAdapter(List<String> locations) {
        this.locations = locations;
    }

    @NonNull
    @Override
    public LocationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_row, parent, false);
        return new LocationsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsAdapter.ViewHolder holder, int position) {
        String location = locations.get(position);
        holder.location.setText(location);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}