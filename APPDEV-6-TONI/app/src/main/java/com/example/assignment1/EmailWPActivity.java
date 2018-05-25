package com.example.assignment1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static java.net.Proxy.Type.HTTP;

public class EmailWPActivity extends AppCompatActivity {

    private static final String TAG = "EmailWPActivity";
    private ClickToContinueFragment clickFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_wp);

        clickFrag = new ClickToContinueFragment();
        clickFrag.btn_text = "Mail Now";

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_button, clickFrag)
                .commit();

    }
}
