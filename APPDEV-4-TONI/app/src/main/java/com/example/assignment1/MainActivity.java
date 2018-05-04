package com.example.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static Button btnEmail;
    private static Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEmail = (Button)findViewById(R.id.btnEmailWP);
        btnCall = (Button)findViewById(R.id.btnCallWP);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEmailPage();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCallPage();
            }
        });

    }

    private void clickEmailPage() {
        Log.d(TAG, "clickEmailPage");

        Intent intent = new Intent("android.intent.action.EmailWPActivity");

        startActivity(intent);
    }

    private void clickCallPage() {
        Log.d(TAG, "clickCallPage");

        Intent intent = new Intent("android.intent.action.CallWPActivity");

        startActivity(intent);
    }
}
