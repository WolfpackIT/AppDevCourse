package com.example.mzfirstspam;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {
    public GeofenceTransitionsIntentService() {
        super("GeoFenceService");
    }

    // ...
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void onHandleIntent(Intent intent) {
        Log.d("Geovent", "geofence triggered");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e("geofenceService", "something wrong here ");
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) { // || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            Geofence geo = triggeringGeofences.get(0);
            String GeoConCat = "";
            for (int i = 0; i< triggeringGeofences.size(); i++) {
                String list = geo.toString();
                GeoConCat = GeoConCat+"    "+list;
            }


            Intent notifyIntent = new Intent(this, MainActivity.class);
// Set the Activity to start in a new, empty task
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notifyIntent.putExtra("Focus", "2");
// Create the PendingIntent
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                    this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
            );
            String CHANNEL_ID = "300A";
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentIntent(notifyPendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Welcome @ Wolfpack " )
                    .setContentText("LocationFound")
                    .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(GeoConCat))
                    .setPriority(NotificationCompat.PRIORITY_MIN);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(4, mBuilder.build());
            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.


            // Send notification and log the transition details.
//            sendNotification(geofenceTransitionDetails);
//            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
//            Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
//                    geofenceTransition));
        }
    }
}