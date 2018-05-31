package com.example.assignment1;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


public class ClickToContinueFragment extends Fragment {

    private static final String TAG = "ClickToContinueFragment";
    public static String btn_text = "";
    private Button btnCallEmail;
    private NotificationManagerCompat notificationManager;

    static final int EMAIL_BEING_SENT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_click_to_continue, container, false);

        btnCallEmail = v.findViewById(R.id.btnClickToConFrag);
        btnCallEmail.setText(btn_text);

        notificationManager = NotificationManagerCompat.from(getActivity());

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

    /***FRAGMENTS FOR MAIL AND EMAIL***/
    public void clickToMailFrag() {
        Log.d(TAG, "clickToMail");
        //Details to send email to rene.le.clercq@wolfpackit.nl
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rene.le.clercq@wolfpackit.nl"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mailing a picture");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I did it, Greetings Rene");

        //startActivity(emailIntent);
        startActivityForResult(emailIntent, EMAIL_BEING_SENT);
    }

    public void clickToCallFrag() {
        Log.d(TAG, "clickToCall");

        Uri number = Uri.parse("tel:040-782 0814");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        startActivity(callIntent);
    }

    /***NOTIFICATION FOR SEND EMAIL***/
    public void sendOnChannelSendEmail() {
        String title = "Sent Email to Rene";
        String message = "Sent an picture to Rene!";

        Notification notification = new NotificationCompat.Builder(getActivity(), App.CHANNEL_SENT_EMAIL)
                .setSmallIcon(R.drawable.ic_child_care_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EMAIL)
                .build();

        notificationManager.notify(1, notification);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMAIL_BEING_SENT && resultCode == RESULT_OK) {
            sendOnChannelSendEmail();
        } else {
            Toast.makeText(getActivity(), "Email not sent", Toast.LENGTH_SHORT).show();
        }
    }
}
