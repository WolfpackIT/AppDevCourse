package com.example.wolfpackapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.wolfpackapp.MainActivityfragments.RSSRecycler;
import com.example.wolfpackapp.StartUpFragments.LoadingScreenFragment;
import com.example.wolfpackapp.StartUpFragments.LoginFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.net.InetAddress;
import java.util.Locale;

public class InitialActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_loading_screen);
        View parentLayout = findViewById(android.R.id.content);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Context context = getApplicationContext();
        if (!checkInternetPermission(this)){
            Snackbar sn = Snackbar.make(parentLayout,"please give us the permission or the app won't run", 1900 );
            sn.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    closeNow();
                }
            }, 2000);   //5 seconds
            closeNow();
        }
        if(!checkPermission(Manifest.permission.ACCESS_WIFI_STATE, this) && !checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, this)){
            Snackbar sn = Snackbar.make(parentLayout,"please give us the permission or the app won't run", 1900 );
            sn.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    closeNow();
                }
            }, 2000);   //5 seconds
//            closeNow();
        }
        if( isNetworkConnected() ){
//            if( !isInternetAvailable() ){
//                Snackbar sn = Snackbar.make(parentLayout,"No internet access," +
//                        " please provide internet access", 1900 );
//                sn.show();
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        closeNow();
//                    }
//                }, 2000);   //5 seconds
//            }
        } else {

        }

        GoogleSignInAccount lastSignedInAccount= GoogleSignIn.getLastSignedInAccount(context);
        if(lastSignedInAccount!=null){
            // user has already logged in, you can check user's email, name etc from lastSignedInAccount
            String email = lastSignedInAccount.getEmail();
            Snackbar sn = Snackbar.make(parentLayout,email, 1900 );
            sn.show();
            //intent to go to main menu.
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
            setContentView(R.layout.activity_main);
            RSSRecycler firstFragment = new RSSRecycler();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainA, firstFragment).commit();
        }else{
            // intent to start login screen
//              Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);

            // Create new fragment and transaction
            setContentView(R.layout.fragment_login);
        }

    }



    public boolean checkPermission(String permission, Activity act){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            updatePermission(permission, act);
        } else {
            // Permission has already been granted
            return true;
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            return false;
        } else {
            // Permission has already been granted
            return true;
        }
    }

    public void updatePermission(String permission, Activity act){
        if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                permission)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(act,
                    new String[]{permission},
                    0001);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    public boolean checkInternetPermission(Activity act){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            updateInternetPermission(act);
        } else {
            // Permission has already been granted
            return true;
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            return false;
        } else {
            // Permission has already been granted
            return true;
        }
    }

    public void updateInternetPermission(Activity act){
        if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                Manifest.permission.INTERNET)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(act,
                    new String[]{Manifest.permission.INTERNET},
                    0001);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("http://www.google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar sn = Snackbar.make(findViewById(android.R.id.content),"Please press back again to exit", 2000);
        sn.show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu, menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });

        return true;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d(id+" menu", "item selected");
        //noinspection SimplifiableIfStatement
        if( id == android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
            Log.d("home shit", "did read the input");
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
