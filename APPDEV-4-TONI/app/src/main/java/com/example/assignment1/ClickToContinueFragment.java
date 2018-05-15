package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ClickToContinueFragment extends Fragment {

    private static final String TAG = "ClickToContinueFragment";
    public static String btn_text = "";
    private Button btnCallEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_click_to_continue, container, false);

        btnCallEmail = v.findViewById(R.id.btnClickToConFrag);
        btnCallEmail.setText(btn_text);

        if (btn_text == "Mail Now"){
            btnCallEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickToMailFrag();
                    Log.d(TAG, "clickToMailFrag");
                }
            });
        } else {
            btnCallEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickToCallFrag();
                    Log.d(TAG, "clickToCallFrag");
                }
            });
        }

        return v;
    }

    public void clickToMailFrag() {
        Log.d(TAG, "clickToMail");
        //Details to send email to rene.le.clercq@wolfpackit.nl
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rene.le.clercq@wolfpackit.nl"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HELLO WORLD");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I did it, Greetings Rene");

        startActivity(emailIntent);
    }

    public void clickToCallFrag() {
        Log.d(TAG, "clickToCall");

        Uri number = Uri.parse("tel:040-782 0814");

		// Formally you would need to have
		// the permission android.Manifest.permission.CALL_PHONE
		// granted by the time you start the intent

		// In reference to this, I also placed a comment
		// in the manifest, in which you would have to
		// add that permission as well.
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        startActivity(callIntent);
    }
}
