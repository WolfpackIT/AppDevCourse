package com.example.mzfirstspam;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mzfirstspam.MainActivity;

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
    int TABCOUNT;
    private Context mContext;

    public DemoCollectionPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        TABCOUNT = 2;
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                MainFragment MF = new MainFragment();
                return MF;
            case 1:
                ListFragment LF = new ListFragment();
                return LF;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return TABCOUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.main);
            case 1:
                return mContext.getString(R.string.shouts);
            default:
                return null;
        }
    }
}