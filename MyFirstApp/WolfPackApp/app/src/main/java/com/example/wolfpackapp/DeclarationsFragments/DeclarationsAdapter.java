package com.example.wolfpackapp.DeclarationsFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.example.wolfpackapp.R;

/**
 * Created by Wolfpack on 4/17/2018.
 */

public class DeclarationsAdapter extends FragmentStatePagerAdapter {
    int TABCOUNT;
    private Context mContext;

    public DeclarationsAdapter(Context context, FragmentManager fm) {
        super(fm);
        TABCOUNT = 2;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new SubmittedFragment();
            case 1:
                return new AcceptedFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TABCOUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //TODO change to strings.xml usage so R.string. ....
        switch (position){
            case 0:
                return mContext.getString(R.string.submitted);
            case 1:
                return mContext.getString(R.string.accepted);
            default:
                return null;
        }
    }

}
