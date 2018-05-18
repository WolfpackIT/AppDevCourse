package nl.wolfpackit.callemailapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ShoutAdapter extends RecyclerView.Adapter<ShoutAdapter.ViewHolder> {

    private List<String> mDataset;

    @NonNull
    @Override
    public ShoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String shout = mDataset.get(position);
        holder.mTextView.setText(shout);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public ShoutAdapter(List dataset) {
        mDataset = dataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView;
        }
    }
}
