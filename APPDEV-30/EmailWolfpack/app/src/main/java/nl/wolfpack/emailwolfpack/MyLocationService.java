package nl.wolfpack.emailwolfpack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class MyLocationService extends Service {

    private static final String TAG = "MyLocationService";
    private static final String CHANNEL_ID = "2244";
	// FEEDBACK EDWIN: initalizing as null is not needed, because by defualt the var is null.
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    private final IBinder mBinder = new LocalBinder();

	// FEEDBACK EDWIN: variables below, private,public protected?
    Location wolfpackLocation;

    NotificationManager mNM;
    boolean notificationShown = false;


    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            float distance = location.distanceTo(wolfpackLocation);
			// FEEDBACK EDWIN: hardcoded 50 should be in stringfile or configfile.
            if(distance < 50 && !notificationShown) {
                Log.d(TAG, String.valueOf(distance));
                showNotification(location);
                notificationShown = true;
            } else {
                mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Log.d(TAG, String.valueOf(distance));
                notificationShown = false;
            }
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    class LocalBinder extends Binder {
        LocationManager getLocationManager() {
            return mLocationManager;
        }

        MyLocationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyLocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

	// FEEDBACK EDWIN: hardcoded text!
		// FEEDBACK EDWIN: you should give back information to the user when on of the catch scenarios takes place. You can generate a default notification for instance informing the user that something went wrong. 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        Log.e(TAG, "onStartCommand");
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
        return START_STICKY;
    }

    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");

        initializeLocationManager();

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

		// FEEDBACK EDWIN: hardcoded text and location
        wolfpackLocation = new Location("wolfpack");
        wolfpackLocation.setLatitude(51.449680);
        wolfpackLocation.setLongitude(5.494753);

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }




	//// FEEDBACK EDWIN: again what about < version 0
    private void showNotification(Location location) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
        }

		// FEEDBACK EDWIN: you can do this way nicer (and more readible) by using a string builder. look it up
        String text = "Latitude: " + location.getLongitude() + " Latitude: " + location.getLongitude() + "\n"
                + "Accuracy: " + location.getAccuracy()  + " Altitude: " +  location.getAltitude();

        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingMainIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentMain, 0);

        Intent intentShouts = new Intent(this, MainActivity.class);
        intentShouts.putExtra("tab", 1);
        intentShouts.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingShoutsIntent = PendingIntent.getActivity(getApplicationContext(), 1, intentShouts, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Welcome back to the office!")
                .setContentText("We are not paying to sit around, get to work...")
                .addAction(R.drawable.ic_launcher_background, "Main", pendingMainIntent)
                .addAction(R.drawable.ic_launcher_background, "Shouts", pendingShoutsIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(123, mBuilder.build());

    }


    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
		// FEEDBACK EDWIN: why call super before your own code, instead of after? there are reasons, but is this random?
        super.onDestroy();
        mNM.cancel(123);
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

}
