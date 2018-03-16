package nl.wolfpack.emailwolfpack;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class EmailWPActivity extends AppCompatActivity {

    private String photoPath;

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
        photoPath = intent.getStringExtra("photoPath");
    }


    private File getImage() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(ContactWolfpackFragment.IMAGE_DIR, Context.MODE_PRIVATE);
        File imagePath = new File(directory, "wolfpack.jpg");
        return imagePath.getAbsoluteFile();
    }

    public void onClickPlaceholder(View view) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("mailto:" + Uri.encode("rene.le.clercq@wolfpackit.nl")));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HELLO WORLD");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "I did it, greetings qasim");
            File f = new File(photoPath);
            Uri photoUri = Uri.fromFile(f);
            emailIntent.putExtra(Intent.EXTRA_STREAM,  photoUri);
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("EmailWPActivity", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
