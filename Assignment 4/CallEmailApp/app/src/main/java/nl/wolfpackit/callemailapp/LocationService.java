package nl.wolfpackit.callemailapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

import java.security.Security;

public class LocationService extends Service {
    private LocationManager locationManager;
    public MyLocationListener listener;
    private Location officeLocation;
    private boolean notifiedUser;
    private final IBinder mBinder = new LocalBinder();
    private final int LOC_INTERVAL = 500;
    private final float LOC_DISTANCE = (float) 20;
    private final int PERM_REQ_COARSE_LOCATION = 911;
    private final String CHANNEL_ID = "GEOFENCE_NOTIFICATION_CHANNEL";
    private NotificationManager notificationManager;
    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        officeLocation.setLatitude(R.string.office_latitude);
        officeLocation.setLongitude(R.string.office_longitude);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO: ADD ACCEPTABLE MESSAGES TO THE EXCEPTIONS. ADD THEM IN R/STRINGS.XML
        super.onStartCommand(intent, flags, startId);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOC_INTERVAL, LOC_DISTANCE, new MyLocationListener(LocationManager.NETWORK_PROVIDER));
        }
        catch(IllegalArgumentException ex) {
            Log.d("onStartCommandNTW", ex.getMessage());
        }
        catch(SecurityException ex) {
            Log.d("onStartCommandNTW", ex.getMessage());
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOC_INTERVAL, LOC_DISTANCE, new MyLocationListener(LocationManager.GPS_PROVIDER));
        }
        catch(SecurityException ex) {
            Log.d("onStartCommandGPS", ex.getMessage());
        }
        catch(IllegalArgumentException ex) {
            Log.d("onStartCommandGPS", ex.getMessage());
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(listener);
        notificationManager.cancel(678);
    }

    private class MyLocationListener implements android.location.LocationListener
    {

        Location lastLocation;

        public MyLocationListener(String provider) {
            lastLocation = new Location(provider);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onLocationChanged(final Location loc)
        {
            lastLocation.set(loc);
            double distanceToWolfpack = lastLocation.distanceTo(officeLocation);
            if (!notifiedUser && distanceToWolfpack < 20) {
                notifyUser(loc);
                notifiedUser = true;
            }
            else {
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notifiedUser = false;
            }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Location Disabled", Toast.LENGTH_SHORT ).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Location Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Toast.makeText( getApplicationContext(), "Status changed", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notifyUser(final Location location) {
        int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
        CharSequence channelName = getString(R.string.channel_name);
        String channelDescription = getString(R.string.channel_description);
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, channelName, channelImportance);
        mChannel.setDescription(channelDescription);
        notificationManager.createNotificationChannel(mChannel);

        String notificationText = "Latitude: " + location.getLongitude() + " Latitude: " + location.getLongitude() + "\n"
                + "Accuracy: " + location.getAccuracy();


        Intent startApp = new Intent(this, MainActivity.class);
        startApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startApp.putExtra("shoutTab", 1);
        PendingIntent pendingIntentStartApp = PendingIntent.getActivity(getApplicationContext(), 1, startApp, 0);

        Intent archiveIntent = new Intent(this, MainActivity.class);
        startApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentArchive = PendingIntent.getActivity(getApplicationContext(), 0, archiveIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Wolfpack")
                .setContentText("Welcome @ Wolfpack !")
                .addAction(R.drawable.ic_launcher_background, "Start app", pendingIntentStartApp)
                .addAction(R.drawable.ic_launcher_background, "Archive", pendingIntentArchive)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(678, mBuilder.build());
    }
    public class LocalBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
        LocationManager getLocationManager() {
            return locationManager;
        }
    }
}
