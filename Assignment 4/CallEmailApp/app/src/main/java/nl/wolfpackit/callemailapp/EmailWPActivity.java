package nl.wolfpackit.callemailapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import static java.net.Proxy.Type.HTTP;

public class EmailWPActivity extends AppCompatActivity {

    private String photoPath;
    private int SENT_MAIL = 1234;
    private String CHANNEL_ID = "EMAIL_CHANNEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_wp);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ClickToContinueFragment fragment = new ClickToContinueFragment();
        fragmentTransaction.add(R.id.fragment, fragment);
        fragmentTransaction.commit();
        Button button = findViewById(R.id.placeholderButton);
        button.setText(R.string.mail_now_button);
        button.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        photoPath = intent.getStringExtra("imagePath");
    }

    public void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"rene.le.clercq@wolfpackit.nl"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "HELLO WORLD");
        intent.putExtra(Intent.EXTRA_TEXT, "I did it, greetings Dragos");
        File picture = new File(photoPath);
        Uri pictureURI = Uri.fromFile(picture);
        intent.putExtra(Intent.EXTRA_STREAM, pictureURI);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Send mail..."), SENT_MAIL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SENT_MAIL) {
            if (resultCode == Activity.RESULT_OK){
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Email sent!")
                        .setContentText("Sent a picture to Rene!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setSmallIcon(R.drawable.ic_launcher_background);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(123, mBuilder.build());
            }

            else {
                Toast.makeText(this, "Email could not be sent", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickPlaceholder(View view) {
        try {
            sendEmail();
        }
        catch (ActivityNotFoundException ex) {
            Log.e("NotFoundException", ex.getMessage());
        }
    }

}
