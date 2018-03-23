package nl.wolfpack.emailwolfpack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nl.wolfpack.emailwolfpack.geofence.GeoFenceFragment;
import nl.wolfpack.emailwolfpack.slack.ShoutFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumberOfTabs;

    public PagerAdapter(FragmentManager fragmentManager, int NumbOfTabs) {
        super(fragmentManager);
        this.mNumberOfTabs = NumbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                return new ContactWolfpackFragment();
            }
            case 1: {
                return new ShoutFragment();
            }
            case 2: {
                return new GeoFenceFragment();
            }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumberOfTabs;
    }
}
