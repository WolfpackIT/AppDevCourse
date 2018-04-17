package com.example.wolfpackapp.DeclarationsFragments;

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
    public DeclarationsAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
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
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //TODO change to strings.xml usage so R.string. ....
        switch (position){
            case 0:
                return "submitted";
            case 1:
                return "accepted";
            default:
                return null;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
