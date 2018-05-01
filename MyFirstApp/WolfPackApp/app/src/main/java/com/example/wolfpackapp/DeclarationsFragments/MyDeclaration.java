package com.example.wolfpackapp.DeclarationsFragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wolfpackapp.DeclarationActivity;
import com.example.wolfpackapp.DeclarationDatabase.DecDB;
import com.example.wolfpackapp.DeclarationDatabase.Declaration;
import com.example.wolfpackapp.DeclarationDatabase.DeclarationCar;
import com.example.wolfpackapp.DeclarationDatabase.DeclarationOther;
import com.example.wolfpackapp.R;
import com.example.wolfpackapp.TogglActivity;
import com.example.wolfpackapp.decleratonRecyclerViewAdapter.DeclarationAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MyDeclaration extends FragmentActivity {


    String NAME = "voornaam";
    String EMAIL = "email";
    String ADMIN = "admin";
    DecDB db;
    static Declaration info;
    static DeclarationCar car;
    static DeclarationOther general;
    static boolean newDec;
    static boolean newDecCar;
    boolean infob;

    DeclarationsAdapterAdapterV2 mDemoCollectionPagerAdapter;
    CustomViewpager mViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_declaration);
        db = Room.databaseBuilder(this,
                DecDB.class, "Declaration").build();
        Intent intent = getIntent();
        Long value = intent.getLongExtra("id", -1);
        Long newID = intent.getLongExtra("new", -1);
        mDemoCollectionPagerAdapter =
                new DeclarationsAdapterAdapterV2(this, getSupportFragmentManager());

        mViewPager = (CustomViewpager) findViewById(R.id.decPager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mViewPager.setClickable(false);
        mViewPager.setEnabled(false);


        if(value >= 0){
            infob = true;
            new getDeclarations().execute(value);
            mViewPager.setPagingEnabled(false);

        } else if (newID > 0){
            car = new DeclarationCar();
            general = new DeclarationOther();
            info.setUid(newID);
            car.setUid(newID);
            infob = false;
            new getDeclarations().execute((long) -5, value);
            new getMaxID().execute();
            mViewPager.setPagingEnabled(true);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.decSlidingTabs);
            tabLayout.setupWithViewPager(mViewPager);

        } else {
            infob = true;
            car = new DeclarationCar();
            info = new Declaration();
            general = new DeclarationOther();
            new getMaxID().execute((long) 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String currentDateandTime = sdf.format(new Date());
            MyDeclaration.info.setTimestamp(currentDateandTime); //TODo set timestamp
            SharedPreferences sharedpref;
            sharedpref = this.getPreferences(Context.MODE_PRIVATE);
            String email;
            email = sharedpref.getString(EMAIL, "test");
            Log.d("mailtest my dec", email);
            MyDeclaration.info.setEmail(email);
            MyDeclaration.info.setChecked(false);
            mViewPager.setPagingEnabled(true);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.decSlidingTabs);
            tabLayout.setupWithViewPager(mViewPager);
        }
    }

    private class getMaxID extends AsyncTask<Long, Integer, Long> {
        String TAGB = "setText";


        @Override
        protected Long doInBackground(Long... value) {
            SharedPreferences sharedpref = getPreferences(Context.MODE_PRIVATE);
            String email = sharedpref.getString(EMAIL, "email");
            Log.d("mailtest my dec backgr", email);
            String name = sharedpref.getString(NAME, "username");
            Boolean admin = sharedpref.getBoolean(ADMIN, false);
            if (infob) {
                long x = db.DecDAO().getMaxID();
                info.setUid(x + 1);
            }
            long y = db.DecDAO().getMaxcID();
            long z = db.DecDAO().getMaxoID();
            car.setCID(y+1);
            general.setOID(z+1);

            db.DecDAO().getMaxcID();
            db.DecDAO().getMaxoID();
            return (long) y;
        }

        @Override
        protected void onPostExecute(Long aLong) {

        }
    }

    private class getDeclarations extends AsyncTask<Long, Integer, Long> {
        String TAGB = "setText";

        @Override
        protected Long doInBackground(Long... value) {

            long id = value[0];
            if(id >= 0) {
                info = db.DecDAO().loadSingleByIds(id);
                car = db.DecDAO().loadSingleCar(id);
                general = db.DecDAO().loadSingleOther(id);
                if (car != null) {
                    Log.d(TAGB, "car is not null");
                    return (long) 2;
                } else if (general != null) {
                    Log.d(TAGB, "other is not null");
                    return (long) 1;
//                mViewPager.setCurrentItem(0);
//                TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//                tabLayout.setupWithViewPager(mViewPager);

                } else {
                    Log.d(TAGB, "something went wrong ");
                }
            } else {

            }
            id = value[1];
            info = new Declaration();
            info = db.DecDAO().loadSingleByIds(id);
//                info.setUid(id);
            newDec = true;
            newDecCar = true;
            Log.d("mydec", "new decs");
            return(long) 0;
        }

        @Override
        protected void onPostExecute(Long aLong) {

            if(aLong == 2) {
                mViewPager.setCurrentItem(1);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.decSlidingTabs);
                tabLayout.setupWithViewPager(mViewPager);
                LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
                for(int i = 0; i < tabStrip.getChildCount(); i++) {
                    tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                    newDecCar = false;
                    newDec = true;
                    Log.d("mydec", "car decs");
                }
            } else if (aLong == 1){
                mViewPager.setCurrentItem(0);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.decSlidingTabs);
                tabLayout.setupWithViewPager(mViewPager);
                LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
                for(int i = 0; i < tabStrip.getChildCount(); i++) {
                    tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                    newDec = false;
                    newDecCar=true;
                    Log.d("mydec", "general/ other decs");
                }
            } else {
                newDec = true;
                newDecCar = true;
                Log.d("mydec", "new decs");
            }

        }
    }
}
