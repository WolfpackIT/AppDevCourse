package nl.wolfpackit.wolfpack.modules.declarations;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.modules.ModuleFragment;

public class DeclarationModule extends ModuleFragment{
    ViewPager viewPager;
    TabLayout tabs;

    public static DeclarationModule createInstance() {
        ModuleFragment f = ModuleFragment.createInstance(R.layout.module_declarations, DeclarationModule.class);
        return (DeclarationModule)f;
    }

    protected void setup() {
        super.setup();

        tabs = (TabLayout) activity.findViewById(R.id.declarationsTab);
        viewPager = (ViewPager) activity.findViewById(R.id.declarationsPages);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab){
                int index = tab.getPosition();
                viewPager.setCurrentItem(index);
            }
            public void onTabUnselected(TabLayout.Tab tab){}
            public void onTabReselected(TabLayout.Tab tab){}
        });

        viewPager.setAdapter(new FragmentStatePagerAdapter(activity.getSupportFragmentManager()){
            public int getCount(){
                return 2;
            }

            public Fragment getItem(int position){
                switch (position) {
                    case 0:
                        return new DeclarationAcceptedListFragment();
                    case 1:
                        return new DeclarationSubmittedListFragment();
                    default:
                        return null;
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                int tabIndex = tabs.getSelectedTabPosition();
                if(tabIndex!=position)
                    tabs.getTabAt(position).select();
            }
        });
    }

    public String getToolbarText(){
        return getString(R.string.nav_declarations);
    }
    public int getToolbar(){
        return R.id.declarationsToolbar;
    }
}
