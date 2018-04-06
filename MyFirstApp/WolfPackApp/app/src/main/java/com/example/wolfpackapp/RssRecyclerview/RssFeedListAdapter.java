package com.example.wolfpackapp.RssRecyclerview;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wolfpackapp.R;

import java.util.List;

/**
 * Created by Wolfpack on 4/3/2018.
 */

public class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private List<RSSFeedModel> mRssFeedModels;

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }

        @Override
        public void onClick(View view) {
            //do something with a click
        }
    }

    public RssFeedListAdapter(List<RSSFeedModel> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RSSFeedModel rssFeedModel = mRssFeedModels.get(position);
        ((TextView)holder.rssFeedView.findViewById(R.id.feedTitle)).setText(rssFeedModel.title);
        ((TextView)holder.rssFeedView.findViewById(R.id.feedDescription))
                .setText(rssFeedModel.description);
        ((TextView)holder.rssFeedView.findViewById(R.id.feedLink)).setText(rssFeedModel.link);
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }
}
