package com.example.wolfpackapp.MainActivityfragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.wolfpackapp.MainActivity;
import com.example.wolfpackapp.R;
import com.example.wolfpackapp.RssRecyclerview.Downloader;


public class RSSRecycler extends Fragment implements View.OnClickListener {
    final static String urlAddress="http://www.rtlnieuws.nl/service/rss/sport/voetbal/index.xml";
    Toolbar mToolbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rssrecycler, container, false);
        android.support.design.widget.AppBarLayout mAppBarLayout;
        mToolbar = (Toolbar) view.findViewById(R.id.rssFeedToolbar);
        initToolbar();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RecyclerView rv= (RecyclerView) getActivity().findViewById(R.id.rv);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setItemAnimator(new DefaultItemAnimator());
                Log.d("RSSFragment", "did read the click");
                new Downloader(getContext(),urlAddress,rv).execute();
            }
        });
//        mToolbar = (Toolbar) getView().findViewById(R.id.rssFeedToolbar);




    }

    @Override
    public void onClick(View view) {

    }

    private void initToolbar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.showOverflowMenu();
        ((AppCompatActivity ) getActivity()).setSupportActionBar(mToolbar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}