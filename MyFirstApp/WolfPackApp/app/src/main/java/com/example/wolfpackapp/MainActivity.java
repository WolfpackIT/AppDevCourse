package com.example.wolfpackapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.wolfpackapp.MainActivityfragments.RSSRecycler;
import com.example.wolfpackapp.RssRecyclerview.Downloader;


public class MainActivity extends FragmentActivity {

    final static String urlAddress="http://www.rtlnieuws.nl/service/rss/sport/voetbal/index.xml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create a new Fragment to be placed in the activity layout
        RSSRecycler firstFragment = new RSSRecycler();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainA, firstFragment).commit();

//        //TODO fix error that comes after this, Activity already has a toolbar already.
////        Toolbar tb = (Toolbar) findViewById(R.id.tool_bar);
////        // Setting toolbar as the ActionBar with setSupportActionBar() call
////        setSupportActionBar(tb);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
//
//        final RecyclerView rv= (RecyclerView) findViewById(R.id.rv);
//        rv.setLayoutManager(new LinearLayoutManager(this));
//        rv.setItemAnimator(new DefaultItemAnimator());
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("rss reader", "activiating");
//                new Downloader(MainActivity.this,urlAddress,rv).execute();
//            }
//        });
    }


}
