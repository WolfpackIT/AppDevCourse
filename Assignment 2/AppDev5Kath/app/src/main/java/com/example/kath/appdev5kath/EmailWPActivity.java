package com.example.kath.appdev5kath;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class EmailWPActivity extends AppCompatActivity {
    private static final String TAG = "EmailWPActivity";
    private ClickToContinueFragment frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"inside OnCreateMail");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_wp);

        frag = new ClickToContinueFragment();
        frag.btnName = "Mail now";

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,frag)
                .commit();
    }
}
