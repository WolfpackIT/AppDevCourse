package com.example.kath.wolfpackassignment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CallWPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_wp);
    }

    public void callNow(View view){
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0407820814"));
        startActivity(callIntent);
    }
}
