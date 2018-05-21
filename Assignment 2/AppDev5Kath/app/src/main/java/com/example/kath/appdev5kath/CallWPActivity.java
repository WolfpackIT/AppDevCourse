package com.example.kath.appdev5kath;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class CallWPActivity extends AppCompatActivity {
    private static final String TAG = "CallWPActivity";
    private ClickToContinueFragment frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"inside OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_wp);

        frag = new ClickToContinueFragment();
        frag.btnName = "Call now";

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,frag)
                .commit();
    }
}
