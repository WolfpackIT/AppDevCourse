package nl.wolfpack.emailwolfpack.slack;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.wolfpack.emailwolfpack.R;

public class ShoutsAdapter extends RecyclerView.Adapter<ShoutsAdapter.ViewHolder> {
    private List<String> shouts;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shout;

        public ViewHolder(View view) {
            super(view);
            shout = (TextView) view.findViewById(R.id.textViewShout);
        }
    }

    public ShoutsAdapter(List<String> shouts) {
        this.shouts = shouts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shout_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String shout = shouts.get(position);
        holder.shout.setText(shout);
    }

    @Override
    public int getItemCount() {
        return shouts.size();
    }
}
