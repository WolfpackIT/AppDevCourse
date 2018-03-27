package nl.wolfpackit.appdev12;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nl.wolfpackit.appdev12.contact.ContactFragment;
import nl.wolfpackit.appdev12.geofence.GeoFenceFragment;
import nl.wolfpackit.appdev12.shout.ShoutFragment;
import nl.wolfpackit.appdev12.wiggle.WiggleFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ContactFragment();
            case 1:
                return new ShoutFragment();
            case 2:
                return new GeoFenceFragment();
            case 3:
                return new WiggleFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
