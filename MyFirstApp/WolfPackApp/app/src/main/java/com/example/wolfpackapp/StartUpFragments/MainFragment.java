package com.example.wolfpackapp.StartUpFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wolfpackapp.DeclarationActivity;
import com.example.wolfpackapp.InitialActivity;
import com.example.wolfpackapp.MainActivityfragments.RSSRecycler;
import com.example.wolfpackapp.R;
import com.example.wolfpackapp.RssRecyclerview.Downloader;
import com.example.wolfpackapp.TogglActivity;
import com.example.wolfpackapp.adminDB.AdpDB;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

/**
 * Created by Wolfpack on 4/17/2018.
 */

public class MainFragment extends Fragment implements DrawerLayout.DrawerListener {

    String NAME = "voornaam";
    String EMAIL = "email";
    String ADMIN = "admin";

    Toolbar mToolbar;
    GoogleSignInClient mGoogleSignInClient;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    AdpDB ad;

    DrawerLayout drawer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ad = Room.databaseBuilder(getContext(),
                AdpDB.class, "Admin").build();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        android.support.design.widget.AppBarLayout mAppBarLayout;
        mToolbar = (Toolbar) view.findViewById(R.id.MainToolbar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String email = sharedpref.getString(EMAIL, "email");
        Log.d("mailtest main", email);
        String name = sharedpref.getString(NAME, "username");
        new verifyAdmin().execute(email);
        final DrawerLayout drawer=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.setDrawerListener(this);
        initToolbar();
        NavigationView navigationView = getView().findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        Log.d("menuItem ", menuItem.getItemId()+" of current item");
                        if(menuItem.getItemId() == R.id.nav_first_fragment) {
                            startNewActivity(getContext(), "nl.akyla.payrollSelect");
                        } else if (menuItem.getItemId() == R.id.nl_switch){
                            setLocale("nl");
                        } else if (menuItem.getItemId() == R.id.en_switch){
                            setLocale("en");
                        } else if (menuItem.getItemId() == R.id.nav_second_fragment) {
                            signOut();
                        } else if (menuItem.getItemId() == R.id.nav_third_fragment) {
                            Intent intent = new Intent(getContext(), DeclarationActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.nav_fourth_fragment) {
                            Intent intent = new Intent(getContext(), TogglActivity.class);
                            startActivity(intent);
                        } else if(menuItem.getItemId() == R.id.navHome){
                            Intent intent = new Intent(getContext(), InitialActivity.class);
                            intent.putExtra("init", (long) 5);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
    }

    private void initToolbar() {
        ImageView iv = (ImageView) getActivity().findViewById(R.id.toolView);
        iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
                mDrawerLayout.openDrawer(GravityCompat.START);

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
        Intent refresh = new Intent(getContext(), InitialActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(refresh);
//        finish();
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
                .addOnCompleteListener( getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        getActivity().setContentView(R.layout.activity_main);
                        SharedPreferences sharedpref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        String email = sharedpref.getString(EMAIL, "email");
                        Log.d("mailtest main logout", email);
                        String name = sharedpref.getString(NAME, "username");


                        SharedPreferences edit = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = edit.edit();
                        editor.putString(EMAIL, "");
                        editor.commit();

                        SharedPreferences edit2 = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = edit2.edit();
                        editor2.putString(NAME, "");
                        editor2.commit();

                        LoginFragment firstFragment = new LoginFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.mainA, firstFragment).commit();
                    }
                });
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        SharedPreferences sharedpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String email = sharedpref.getString(EMAIL, "email");
        Log.d("mailtest main tab", email);
        String name = sharedpref.getString(NAME, "username");

        Log.d("mailtest main tab", email);
        Log.d("main sso name",""+name);
        TextView emtv = (TextView) getActivity().findViewById(R.id.textViewEmailNav);
        TextView ustv = (TextView) getActivity().findViewById(R.id.textViewUsernameNav);
        emtv.setText(email);
        ustv.setText(name);
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private class verifyAdmin extends AsyncTask<String, Integer, Long> {
        String TAGB = "loginbackground";

        @Override
        protected Long doInBackground(String... strings) {
            if (ad.AdDAO().findByName(strings[0]) != null)  {
                Snackbar sn = Snackbar.make(getView(),"welcome admin", 1000 );
                sn.show();
                SharedPreferences edit2 = getActivity().getPreferences(Context.MODE_PRIVATE);
                String email = edit2.getString(EMAIL, "email");
                Log.d("mailtest main back", email);
                SharedPreferences.Editor editor2 = edit2.edit();
                editor2.putBoolean(ADMIN, true);
                editor2.putString(EMAIL, email);
                editor2.commit();
                Log.d("sso admin", "admin logged in");
                return (long) 1;
            }
            return (long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Log.d("sso admin", "admin logged in");
            Log.d("sso admin", "admin logged in");
            if (aLong > 0){
                Snackbar sn = Snackbar.make(getView(),"welcome admin", 1000 );
                sn.show();
                SharedPreferences sharedpref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String email = sharedpref.getString(EMAIL, "email");
                Log.d("mailtest main back post", email);
                String name = sharedpref.getString(NAME, "username");
                Boolean x = sharedpref.getBoolean(ADMIN, false);


                Log.d("sso ad", "onPostExecute: succes ");
            }
        }
    }
}
