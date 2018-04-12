package com.example.wolfpackapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.wolfpackapp.MainActivityfragments.RSSRecycler;
import com.example.wolfpackapp.StartUpFragments.LoadingScreenFragment;
import com.example.wolfpackapp.StartUpFragments.LoginFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.net.InetAddress;

public class InitialActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_loading_screen);
        View parentLayout = findViewById(android.R.id.content);

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
}
