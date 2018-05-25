package nl.wolfpackit.callemailapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements shoutFragment.OnFragmentInteractionListener,
        OldMainActivity.OnFragmentInteractionListener, GeofencingFragment.OnFragmentInteractionListener {

    private final int NUM_PAGES = 3;
    private Intent locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TabLayout tabLayout = findViewById(R.id.tabLayout1);
        tabLayout.addTab(tabLayout.newTab().setText("Contact Wolfpack"));
        tabLayout.addTab(tabLayout.newTab().setText("Shout!"));

        ViewPager mViewPager = findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        locationService = new Intent(this, LocationService.class);
        ContextCompat.startForegroundService(getApplicationContext(), locationService);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private Context mContext;

        public ScreenSlidePagerAdapter(Context context, FragmentManager fm){
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new OldMainActivity();
                    break;
                case 1:
                    fragment = new shoutFragment();
                    break;
                default:
                    fragment = new GeofencingFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.contact_wolfpack_tab);
                case 1:
                    return mContext.getString(R.string.shout_tab);
                case 2:
                    return mContext.getString(R.string.geofencing);
                default:
                    return null;
            }
        }
    }
}
