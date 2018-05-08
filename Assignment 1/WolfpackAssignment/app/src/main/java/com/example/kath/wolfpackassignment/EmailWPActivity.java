package com.example.kath.wolfpackassignment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EmailWPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_wp);
    }

    public void mailNow(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:" + "rene.le.clercq@wolfpackit.nl'?"+"subject=" + "HELLO WORLD" + "&body=" + "I did it, greetings Kath ");
        intent.setData(data);
        startActivity(intent);
    }

    public void mailNowI(View view){
        Intent myIntent = new Intent(this, EmailWPActivity.class);
        
    }
 /*   public void mailNow(String address, String subject, String text)
    {
        Intent intent = new Intent(this, EmailWPActivity.class);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, address );
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {

            startActivity(Intent.createChooser(intent, "Send email..."));
        }
    }*/
}
