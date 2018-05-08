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

public class CallWPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_wp);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ClickToContinueFragment fragment = new ClickToContinueFragment();
        fragmentTransaction.add(R.id.fragment2, fragment);
        fragmentTransaction.commit();
        Button button = findViewById(R.id.placeholderButton);
        button.setText(R.string.call_now_button);
        button.setVisibility(View.VISIBLE);

    }

    public void initiatePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0407820814"));
		
		// Formally you would need to have
		// the permission android.Manifest.permission.CALL_PHONE
		// granted by the time you start the intent

		// In reference to this, I also placed a comment
		// in the manifest, in which you would have to
		// add that permission as well.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void clickPlaceholder(View view) {
        try {
            initiatePhoneCall();
        }
        catch (ActivityNotFoundException ex) {
            Log.e("exception", ex.getMessage());
        }
    }
}
