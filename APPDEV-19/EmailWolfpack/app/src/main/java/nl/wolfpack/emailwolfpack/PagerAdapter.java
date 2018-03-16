package nl.wolfpack.emailwolfpack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
                ContactWolfpackFragment contactWolfpackFragment = new ContactWolfpackFragment();
                return contactWolfpackFragment;
            }
            case 1: {
                ShoutFragment shoutsTab = new ShoutFragment();
                return shoutsTab;
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
