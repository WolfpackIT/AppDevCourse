package com.example.mzfirstspam;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Wolfpack on 3/30/2018.
 */

public class Dismiss extends Activity {

        public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, 4));
            finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
        }


}
