package nl.wolfpackit.callemailapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

import static java.net.Proxy.Type.HTTP;

public class EmailWPActivity extends AppCompatActivity {

    private String photoPath;

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
            startActivity(Intent.createChooser(intent, "send mail..."));
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
