package com.example.mzfirstspam;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;

import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        startActivityForResult(Intent.createChooser(emailIntent, "send shit"),1 );
    }

    public void onActivityResult(int req, int res, Intent data) {
        Log.d("a", "something "+res);

        String CHANNEL_ID = "500A";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon()
                .setContentTitle("Sent an picture to Rene!")
                .setContentText("Rene has received a beautiful picture of you. Well Done!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

         NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(this);
         notificationManagerCompat.notify(5,mBuilder.build());



      //  File test = new File(location);
      //  Uri path = Uri.fromFile(test);


    }
}
