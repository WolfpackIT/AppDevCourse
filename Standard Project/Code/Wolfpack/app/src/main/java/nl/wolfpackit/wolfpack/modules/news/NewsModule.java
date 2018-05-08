package nl.wolfpackit.wolfpack.modules.news;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import nl.wolfpackit.wolfpack.ImageTools;
import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.database.Database;
import nl.wolfpackit.wolfpack.database.Declaration;
import nl.wolfpackit.wolfpack.database.News;
import nl.wolfpackit.wolfpack.database.Receipt;
import nl.wolfpackit.wolfpack.modules.ModuleFragment;

public class NewsModule extends ModuleFragment{
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    NewsAdapter adapter;

    public static NewsModule createInstance(){
        ModuleFragment f = ModuleFragment.createInstance(R.layout.module_news, NewsModule.class);
        return (NewsModule)f;
    }

    @SuppressLint("StaticFieldLeak")
    protected void setup(){
        super.setup();
//        Log.w("test", account+"");
//        if(account!=null)
//            Log.w("test", account.getName());
//        account.setName("Tar van Krieken");
//        account.update();

//        Database.getDeclarationsPending(new Database.QueryResult<List<Declaration>>() {
//            public void onResult(List<Declaration> o) {
//                for(Declaration dec: o){
//                    Log.w("testing", dec.getAmount()+"");
//                    dec.getReceipt(new Database.QueryResult<Receipt>() {
//                        public void onResult(Receipt receipt) {
//                            if(receipt!=null)
//                                Log.w("testing", receipt.getName());
//                        }
//                    });
//                }
//            }
//        });
//
//        Database.createDeclaration(new Database.QueryResult<Declaration>() {
//            public void onResult(Declaration dec) {
//                dec.setAmount(90);
//                dec.update();
//                dec.createReceipt(new Database.QueryResult<Receipt>() {
//                    public void onResult(Receipt receipt) {
//                        receipt.setName("peanutbutter");
//                    }
//                });
//            }
//        });


//        //create some news_article
//        ImageTools.downloadImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png", new ImageTools.ImageResultListener(){
//            public void onImageRecieve(Bitmap image){
//                final String imgString = ImageTools.getStringFromImage(image);
//                Database.createNews(new Database.QueryResult<News>() {
//                    public void onResult(News n) {
//                        n.setImageData(imgString);
//                        n.setTitle("testers");
//                        n.setContent("This is some cool <b>test</b> that includes some <span style=background-color:orange>HTML</span> content <img src='http://mrclown.tv/wp-content/uploads/2014/11/MC_SightWords-Some.jpg'/>");
//                        n.setSummery("This is some summary that will be generated to be a short version of the content that has an image");
//                        n.setAuthorID(account.getGoogleID());
//                        n.update();
//                        Log.w("INSERTED", "some thing");
//                    }
//                });
//            }
//        });


        mRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter = new NewsAdapter();
        mRecyclerView.setAdapter(adapter);
    }


    public ActionBar setToolbar() {
        android.support.v7.app.ActionBar actionbar = super.setToolbar();
//        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setTitle(R.string.nav_news);
        return actionbar;
    }

    public int getToolbar(){
        return R.id.newsToolbar;
    }
}
