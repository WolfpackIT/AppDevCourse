package com.example.kath.appdev_4_kath;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClickEmail(View view) {
        Log.d(TAG, "onClickEmail");
        Intent i = new Intent(MainActivity.this, EmailWPActivity.class);
        startActivity(i);
    }


    public void onClickCall(View view) {
        Log.d(TAG, "onClickCall");
        Intent i = new Intent(MainActivity.this, CallWPActivity.class);
        startActivity(i);
    }
}
