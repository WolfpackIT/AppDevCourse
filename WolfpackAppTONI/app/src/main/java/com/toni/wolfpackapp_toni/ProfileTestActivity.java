package com.toni.wolfpackapp_toni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileTestActivity extends AppCompatActivity {

    TextView textName;
    Button btnSignOut;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_test);

        mAuth = FirebaseAuth.getInstance();

        textName = findViewById(R.id.textViewName);
        btnSignOut = findViewById(R.id.btn_SignOut);

        FirebaseUser user = mAuth.getCurrentUser();

        textName.setText("Logged in as " + user.getDisplayName());

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void SignOut() {
        mAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
    }
}
