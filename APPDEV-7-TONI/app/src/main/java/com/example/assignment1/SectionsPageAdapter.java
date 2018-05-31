package com.example.assignment1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wolfpack on 5/24/2018.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {

    //Keeping track of the fragments
    private final List<Fragment> mFragmentList = new ArrayList<>();
    //Keeping track of the fraqments names
    private final List<String> mFragmentTitleList = new ArrayList<>();

    //Adding the fragments to the list
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
