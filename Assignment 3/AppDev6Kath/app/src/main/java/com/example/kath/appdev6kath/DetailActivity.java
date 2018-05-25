package com.example.kath.appdev6kath;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.kath.appdev6kath.MainActivity.EXTRA_SHOUT;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String shoutName = intent.getStringExtra(EXTRA_SHOUT);

        TextView textViewShouts = findViewById(R.id.text_view_shout_detail);
        textViewShouts.setText(shoutName);
    }
}
