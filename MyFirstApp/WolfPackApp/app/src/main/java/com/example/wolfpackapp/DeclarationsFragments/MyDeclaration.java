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


public class MyDeclaration extends FragmentActivity {


    String NAME = "voornaam";
    String EMAIL = "email";
    String ADMIN = "admin";
    DecDB db;
    static Declaration info;
    static DeclarationCar car;
    DeclarationOther general;

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



        mDemoCollectionPagerAdapter =
                new DeclarationsAdapterAdapterV2(this, getSupportFragmentManager());

        mViewPager = (CustomViewpager) findViewById(R.id.decPager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mViewPager.setClickable(false);
        mViewPager.setEnabled(false);


        if(value >= 0){
            new getDeclarations().execute(value);
            mViewPager.setPagingEnabled(false);



        } else {
            mViewPager.setPagingEnabled(true);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.decSlidingTabs);
            tabLayout.setupWithViewPager(mViewPager);
        }



    }

    private class getDeclarations extends AsyncTask<Long, Integer, Long> {
        String TAGB = "setText";

        @Override
        protected Long doInBackground(Long... value) {
            long id = value[0];
            info = db.DecDAO().loadSingleByIds(id);
            car = db.DecDAO().loadAllByCarIds(id);
            general = db.DecDAO().loadAllByOtherIds(id);
            if (car != null){

                return (long) 2;
            } else if (general != null ){
                return (long) 1;
//                mViewPager.setCurrentItem(0);
//                TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//                tabLayout.setupWithViewPager(mViewPager);

            } else {
                Log.d(TAGB, "something went wrong ");
            }
            return (long) 0;
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
                }
            }

        }
    }
}
