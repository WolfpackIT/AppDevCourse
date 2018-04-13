package com.example.wolfpackapp.MainActivityfragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.wolfpackapp.InitialActivity;
import com.example.wolfpackapp.MainActivity;
import com.example.wolfpackapp.R;
import com.example.wolfpackapp.RssRecyclerview.Downloader;

import java.util.Locale;


public class RSSRecycler extends Fragment implements View.OnClickListener {
    final static String urlAddress="http://www.rtlnieuws.nl/service/rss/sport/voetbal/index.xml";
    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;

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
        NavigationView navigationView = getView().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        Log.d("menuItem ", menuItem.getItemId()+" of current item");
                        if(menuItem.getItemId() == R.id.nav_first_fragment) {
                            startNewActivity(getContext(), "nl.akyla.payrollSelect");
                        } else if (menuItem.getItemId() == R.id.nl_switch){
                            setLocale("nl");
                        } else if (menuItem.getItemId() == R.id.en_switch){
                            setLocale("en");
                        }
                        return true;
                    }
                });
//        mToolbar = (Toolbar) getView().findViewById(R.id.rssFeedToolbar);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getContext(), InitialActivity.class);
        startActivity(refresh);
//        finish();
    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {

    }

    private void initToolbar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.app_name);
//        mToolbar.showOverflowMenu();
        ((AppCompatActivity ) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.common_google_signin_btn_text_light);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        navigationView = getView().findViewById(R.id.nav_view);
//        Menu menu = navigationView.getMenu();
//        String lang = Locale.getDefault().getLanguage();
//        if (lang.equals("en")){
//            MenuItem m1 = menu.findItem(R.id.en_switch);
//            m1.setChecked(true);
//            MenuItem m2 = menu.findItem(R.id.nl_switch);
//            m2.setChecked(false);
//        } else if (lang.equals("nl")){
//            MenuItem m1 = menu.findItem(R.id.en_switch);
//            m1.setChecked(false);
//            MenuItem m2 = menu.findItem(R.id.nl_switch);
//            m2.setChecked(true);
//        } else {
//            MenuItem m1 = menu.findItem(R.id.en_switch);
//            m1.setChecked(true);
//            MenuItem m2 = menu.findItem(R.id.nl_switch);
//            m2.setChecked(false);
//        }
    }

}