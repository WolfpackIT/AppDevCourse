package com.example.mzfirstspam;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MailActivity extends AppCompatActivity {
    public String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        Button mailnow = (Button) findViewById(R.id.button4);
        mailnow.setText(R.string.mail_now);
        Intent intent = getIntent();
        location = intent.getStringExtra("picLoc");
    }

    public void doSomething(View view){
        File test = new File(location);
        Uri path = Uri.fromFile(test);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: rene.le.clercq@wolfpackit.nl"));
// The intent does not have a URI, so declare the "text/plain" MIME type
        //emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"rene.le.clearcq@wolfpackit.nl"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HELLO WORLD");
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "i did it, greetings Martijn Ras");
// You can also attach multiple items by passing an ArrayList of Uris
        startActivity(emailIntent);
    }
}
