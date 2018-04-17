package com.example.wolfpackapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.wolfpackapp.DeclarationsFragments.DeclarationsAdapter;
import com.example.wolfpackapp.StartUpFragments.LoginFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class DeclarationActivity extends FragmentActivity {

    DeclarationsAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    GoogleSignInClient mGoogleSignInClient;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration);
        mDemoCollectionPagerAdapter =
                new DeclarationsAdapter(getSupportFragmentManager());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        initToolbar();
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        Log.d("menuItem ", menuItem.getItemId()+" of current item");
                        if(menuItem.getItemId() == R.id.nav_first_fragment) {
                            startNewActivity(getApplicationContext(), "nl.akyla.payrollSelect");
                        } else if (menuItem.getItemId() == R.id.nl_switch){
                            setLocale("nl");
                        } else if (menuItem.getItemId() == R.id.en_switch){
                            setLocale("en");
                        } else if (menuItem.getItemId() == R.id.nav_second_fragment) {
                            signOut();
                        } else if (menuItem.getItemId() == R.id.nav_third_fragment) {
                            Intent intent = new Intent(getApplicationContext() , DeclarationActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.nav_fourth_fragment) {
                            Intent intent = new Intent( getApplicationContext() ,TogglActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, InitialActivity.class);
        startActivity(refresh);
//        finish();
    }

    private void initToolbar() {
        ImageView iv = (ImageView) findViewById(R.id.toolView);
        iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mDrawerLayout = findViewById(R.id.drawer_layout);
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener( this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        setContentView(R.layout.activity_main);
                        LoginFragment firstFragment = new LoginFragment();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.mainA, firstFragment).commit();
                    }
                });
    }

}
