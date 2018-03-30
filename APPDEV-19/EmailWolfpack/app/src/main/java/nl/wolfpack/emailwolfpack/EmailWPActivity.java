package nl.wolfpack.emailwolfpack;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class EmailWPActivity extends AppCompatActivity {

    private final Integer EMAIL_INTENT = 525;
    private final String CHANNEL_ID = "EMAIL";
    private String _photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_wp);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ClickToContinueFragment clickToContinueFragment = new ClickToContinueFragment();
        fragmentTransaction.add(R.id.fragment3, clickToContinueFragment);
        fragmentTransaction.commit();

        Button button = (Button) findViewById(R.id.placeholderButton);
        button.setText(R.string.mail_now_text);

        Intent intent = getIntent();
        _photoPath = intent.getStringExtra(getString(R.string.photoPath));
    }


    private File getImage() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(ContactWolfpackFragment.IMAGE_DIR, Context.MODE_PRIVATE);
        File imagePath = new File(directory, "wolfpack.jpg");
        return imagePath.getAbsoluteFile();
    }

    public void onClickPlaceholder(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:" + Uri.encode(getString(R.string.rene_email))));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.hello_world));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.i_did_it));
        File f = new File(_photoPath);
        Uri photoUri = Uri.fromFile(f);
        emailIntent.putExtra(Intent.EXTRA_STREAM,  photoUri);
        startActivityForResult(emailIntent, EMAIL_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EMAIL_INTENT && resultCode == Activity.RESULT_OK) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(getString(R.string.wolfpack))
                    .setContentText("Email has been sent to rene!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat  notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(544, builder.build());
        } else {
            Toast.makeText(this, R.string.email_not_sent, Toast.LENGTH_SHORT).show();
        }
    }
}
