package nl.wolfpackit.appdev12;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import nl.wolfpackit.appdev12.geofence.GeoFenceFragment;
import nl.wolfpackit.appdev12.geofence.LocationTrackerService;

public class MainActivity extends AppCompatActivity{
    public static MainActivity instance;
    ViewPager viewPager;
    boolean isWiggleRunning;

    public static SharedPreferences mPrefs;
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);

        Intent creationIntent = getIntent();
        int gotoTab = creationIntent.getIntExtra("tab", 0);

        mPrefs = getSharedPreferences("prefs", 0); //init preferences

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        String[] tabNames = getString(R.string.tabNames).split(",");
        for(int i=0; i<tabNames.length; i++){
            tabLayout.addTab(tabLayout.newTab().setText(tabNames[i]));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new nl.wolfpackit.appdev12.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        final View view = this.findViewById(android.R.id.content);
        viewPager.setOffscreenPageLimit(4); //ensures that all fragments are create on fragment opening
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                View target = view.findFocus();
                if (target != null) {
                    InputMethodManager imm = (InputMethodManager) target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
                }
                invalidateOptionsMenu();
            }
            public void onTabUnselected(TabLayout.Tab tab) {}
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        notificationSetup();
        if(gotoTab!=0)
            viewPager.setCurrentItem(gotoTab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(viewPager.getCurrentItem()==3){
            getMenuInflater().inflate(R.menu.menu_wiggle, menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.setWiggle){
            isWiggleRunning = mPrefs.getBoolean("wiggleRunning", false);
            isWiggleRunning = !isWiggleRunning;
            mPrefs.edit().putBoolean("wiggleRunning", isWiggleRunning).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    public void notificationSetup(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(name, name, importance);

            //set some options for the channel
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotificationManager.createNotificationChannel(channel);

            mNotificationManager.cancel(LocationTrackerService.inRangeNotificationID); //remove in range notification when opening the app
        }
    }
    public static String getStringByID(int id){
        return instance.getString(id);
    }
    public static void createSimpleNotification(String title, String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = instance.getString(R.string.channel_name);
            String channelID = (String)name;

            //create notification
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(instance, channelID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // send notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(instance);
            notificationManager.notify(0, mBuilder.build());
        }
    }


    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == GeoFenceFragment.REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GeoFenceFragment.instance.createFlag();
            }else{
                // Permission was denied or request was cancelled
            }
        }

    }
}