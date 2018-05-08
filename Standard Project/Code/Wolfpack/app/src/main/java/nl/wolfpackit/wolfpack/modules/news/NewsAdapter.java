package nl.wolfpackit.wolfpack.modules.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.wolfpackit.wolfpack.ImageTools;
import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.database.Database;
import nl.wolfpackit.wolfpack.database.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    List<News> news = new ArrayList<News>();

    public NewsAdapter(){
        Database.getNews(new Database.QueryResult<List<News>>() {
            public void onResult(List<News> o) {
                news = o;
                notifyDataSetChanged();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_article, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final News news = this.news.get(position);
        ImageView image = (ImageView) holder.view.findViewById(R.id.newsImage);
        image.setImageBitmap(news.getImage());

        final ImageView authorImage = (ImageView) holder.view.findViewById(R.id.newsAuthor);
        news.getAuthorImage(new ImageTools.ImageResultListener(){
            public void onImageRecieve(Bitmap image){
                authorImage.setImageBitmap(image);
            }
        });

        ((TextView)holder.view.findViewById(R.id.newsTitle)).setText(news.getTitle());
        ((TextView)holder.view.findViewById(R.id.newsContent)).setText(news.getSummery());

        holder.view.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Context context = holder.view.getContext();
                Intent intent = new Intent(context, NewsArticleActivity.class);
                intent.putExtra("newsID", news.getID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }
}
