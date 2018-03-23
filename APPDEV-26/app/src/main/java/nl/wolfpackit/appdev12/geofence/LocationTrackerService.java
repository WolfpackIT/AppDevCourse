package nl.wolfpackit.appdev12.geofence;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import nl.wolfpackit.appdev12.MainActivity;
import nl.wolfpackit.appdev12.R;

public class LocationTrackerService extends Service{
    LocationManager locationManager;
    LocationListener locationListenerGPS;
    SharedPreferences mPrefs;
    boolean isInRange = false;
    public static int inRangeNotificationID = 4;
    private FusedLocationProviderClient mFusedLocationClient;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        locationListenerGPS = new LocationListener() {
            public void onLocationChanged(android.location.Location location) {
                checkLocation(location);
            }
            public void onStatusChanged(String provider, int status, Bundle extras){}
            public void onProviderEnabled(String provider){}
            public void onProviderDisabled(String provider){}
        };

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS); //for testing
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 5, locationListenerGPS);
        } catch (java.lang.SecurityException ex) {
            //handle security issue
        }
        return START_STICKY;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(9, new NotificationCompat.Builder(getApplication(), getString(R.string.channel_name))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.locationServiceNotificationTitle))
                .setContentText(getString(R.string.locationServiceNotificationText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build());
        mPrefs = getSharedPreferences("prefs", 0);
    }


    protected void checkLocation(Location loc){
        double lat = loc.getLatitude();
        double lon = loc.getLongitude();

        //load fences from memory
        String[] fenceData = mPrefs.getString("fences", "").split(",");
        List<Fence> fenceList = new ArrayList<Fence>();
        if(fenceData[0].length()>0)
            for(int i=0; i<fenceData.length; i++){
                fenceList.add(new Fence(fenceData[i]));
            }

        FenceListAdapter fences = new FenceListAdapter(fenceList, getApplication());


        //check if in range
        boolean isInRange = fences.checkLocation(lat, lon);
        if(!this.isInRange && isInRange){
            sendInRangeNotification(loc);
        }

        this.isInRange = isInRange;
    }

    protected void sendInRangeNotification(Location loc){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Application application = getApplication();

            String name = application.getString(R.string.channel_name);
            String channelID = (String)name;

            // Create goto app intent
            Intent gotoAppIntent = new Intent(application, MainActivity.class);
            gotoAppIntent.setAction(application.getString(R.string.inRangeNotificationButton));
            gotoAppIntent.putExtra("tab", 1);

            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(application);
            stackBuilder.addNextIntentWithParentStack(gotoAppIntent);

            // Get the PendingIntent containing the entire back stack
            PendingIntent gotoAppPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            //create archive intent, press will get received by BroadcastReceiver
            String EXTRA_NOTIFICATION_ID = "inRangeNotification";
            Intent archiveIntent = new Intent(application, BroadcastReceiver.class);
            archiveIntent.setAction(application.getString(R.string.inRangeNotificationArchiveButton));
            archiveIntent.putExtra(EXTRA_NOTIFICATION_ID, inRangeNotificationID);
            PendingIntent archivePendingIntent =
                    PendingIntent.getBroadcast(application, 1, archiveIntent, 0);

            //create notification
            DecimalFormat decimalFormat = new DecimalFormat(".#####");
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(application, channelID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(application.getString(R.string.inRangeNotificationTitle))
                    .setContentText(application.getString(R.string.inRangeNotificationText))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(application.getString(R.string.inRangeNotificationTextExpanded)
                                    .replace("[long]", decimalFormat.format(loc.getLongitude()))
                                    .replace("[lat]", decimalFormat.format(loc.getLatitude()))
                                    .replace("[alt]", decimalFormat.format(loc.getAltitude()))
                                    .replace("[acc]", decimalFormat.format(loc.getAccuracy()))
                                    .replace("[speed]", decimalFormat.format(loc.getSpeed()))))
                    .addAction(R.drawable.ic_launcher_background, application.getString(R.string.inRangeNotificationButton), gotoAppPendingIntent)
                    .addAction(R.drawable.ic_launcher_background, application.getString(R.string.inRangeNotificationArchiveButton), archivePendingIntent);

            // Register the channel with the system
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(application);
            Notification notification = mBuilder.build();
            notificationManager.notify(inRangeNotificationID, notification);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListenerGPS);
    }
}