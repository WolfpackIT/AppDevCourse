package nl.wolfpackit.wolfpack.modules.news;

import android.annotation.SuppressLint;
import android.arch.persistence.room.util.StringUtil;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

import nl.wolfpackit.wolfpack.ImageTools;
import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.StringTools;
import nl.wolfpackit.wolfpack.database.Database;
import nl.wolfpackit.wolfpack.database.News;

public class NewsArticleActivity extends AppCompatActivity {
    public String newsID;
    public News news;
    public WebView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //add back navigation
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("");

        //load article
        Intent intent = getIntent();
        newsID = intent.getStringExtra("newsID");
        Database.getNews(newsID, new Database.QueryResult<News>() {
            public void onResult(News o) {
                news = o;

                //increase view counter
                news.setViews(news.getViews()+1);
                news.update();

                //load data
                setData();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = super.onOptionsItemSelected(item);
        int ID = item.getItemId();
        switch(ID){
            case android.R.id.home:
                finish();
                return true;
        }
        return ret;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_options, menu);
        return true;
    }

    protected void setData(){
        //set image
        LinearLayout image = findViewById(R.id.articleBackgroundImage);
        image.setBackground(new BitmapDrawable(getResources(),news.getImage()));

        //data
        TextView data = findViewById(R.id.articleData);
        String dataTemplate = getString(R.string.article_data);
        dataTemplate = dataTemplate.replace("[times]", news.getViews()+"");

        final int daysAgo = (int)((System.currentTimeMillis()-news.getDate())/1000d/60d/60d/24d);
        dataTemplate = StringTools.replace(dataTemplate, "\\((.*)\\)", new StringTools.RegexReplacer(){
            public String replace(String... groups){
                return daysAgo!=1?groups[1]:"";
            }
        });
        dataTemplate = dataTemplate.replace("[days]", daysAgo+"");
        data.setText(dataTemplate);

        //set title
        TextView title = findViewById(R.id.articleTitle);
        title.setText(news.getTitle());

        //set author
        final ImageView authorImage = findViewById(R.id.articleAuthor);
        news.getAuthorImage(new ImageTools.ImageResultListener(){
            public void onImageRecieve(Bitmap image){
                authorImage.setImageBitmap(image);
            }
        });

        //set content
        content = findViewById(R.id.articleContent);
        content.setBackgroundColor(0x00000000);
        content.setVerticalScrollBarEnabled(false);
        String contentCode = StringTools.join("\n", new String[]{ //some css and javascript to make you not notice it is a webview
                "<style>",
                "   html,body{",
                "       width:100%;",
                "       height:100%;",
                "       margin:0px;",
                "       font-family: Roboto thin, Roboto",
                "   }",
                "</style>",
        });

        content.loadData(news.getContent()+contentCode,"text/html", null);
    }
}
