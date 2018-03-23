package nl.wolfpack.emailwolfpack;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import android.location.LocationListener;

import com.google.android.gms.location.LocationRequest;

import nl.wolfpack.emailwolfpack.geofence.GeoFenceFragment;

import nl.wolfpack.emailwolfpack.MyLocationService.LocalBinder;


public class MainActivity extends AppCompatActivity implements ContactWolfpackFragment.OnFragmentInteractionListener, GeoFenceFragment.OnFragmentInteractionListener {
    private final static int PERMISSIONS_REQUEST_COARSE_LOCATION = 211;
    private final static int PERMISSIONS_REQUEST_FINE_LOCATION = 212;

    private final static String TAG = "MainActivity";

    TabLayout tabLayout;

    Intent locationServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Wolfpack"));
        tabLayout.addTab(tabLayout.newTab().setText("Shout!"));
        tabLayout.addTab(tabLayout.newTab().setText("GeoFencing"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        int selectedTabIndex;
        if(getIntent().hasExtra("tab")) {
            selectedTabIndex = getIntent().getIntExtra("tab", 0);
            TabLayout.Tab tab = tabLayout.getTabAt(selectedTabIndex);
            Log.d(TAG, "Intent Tab Index: " + String.valueOf(tab.getPosition()));
            tab.select();
        } else {
            Log.d(TAG, "No extra tab");
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            getLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationUpdates();
                } else {
                    Toast.makeText(getApplicationContext(), "Mijn maneer/vrouw we need your location...", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(this, MyLocationService.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Mijn maneer/vrouw we need your location...", Toast.LENGTH_SHORT).show();
                }
            }
            default:
                break;
        }
    }


    private void getLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Log.d("getLocationUpdates", "created");

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.d("getLocationUpdates", "location changed");
//                locationUpdated(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("getLocationUpdates", "status changed");
            }

            public void onProviderEnabled(String provider) {
                Log.d("getLocationUpdates", "provider enabled");
            }

            public void onProviderDisabled(String provider) {
                Log.d("getLocationUpdates", "provider disabled");
            }
        };

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int selectedTabIndex;
        if(intent.hasExtra("tab")) {
            selectedTabIndex = intent.getIntExtra("tab", 0);
            TabLayout.Tab tab = tabLayout.getTabAt(selectedTabIndex);
            Log.d(TAG, "Intent Tab Index: " + String.valueOf(tab.getPosition()));
            tab.select();
        } else {
            Log.d(TAG, "No extra tab");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        locationServiceIntent = new Intent(this, MyLocationService.class);
        ContextCompat.startForegroundService(getApplicationContext(), locationServiceIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(locationServiceIntent);
    }




    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
