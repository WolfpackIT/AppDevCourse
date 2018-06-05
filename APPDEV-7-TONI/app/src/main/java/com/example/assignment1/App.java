package com.example.assignment1;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * Created by Wolfpack on 5/25/2018.
 */

public class App extends Application {
    /** This is to start the Notification Channel has the app is running**/

    public static final String CHANNEL_SENT_EMAIL = "sentEmail";
    public static final String CHANNEL_GET_FENCE = "getFence";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChanel();
    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Notification Channel for Sent Email
            NotificationChannel channelSentEmail = new NotificationChannel(
                    CHANNEL_SENT_EMAIL,
                    "Channel Sent Email",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelSentEmail.setDescription("This is Channel SENT EMAIL");
            //Notification Channel for Get Fence
            NotificationChannel channelGetFence = new NotificationChannel(
                    CHANNEL_GET_FENCE,
                    "Channel Get Fence",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelGetFence.setDescription("This is Channel GET FENCE");


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelSentEmail);
            manager.createNotificationChannel(channelGetFence);
        }
    }
}
