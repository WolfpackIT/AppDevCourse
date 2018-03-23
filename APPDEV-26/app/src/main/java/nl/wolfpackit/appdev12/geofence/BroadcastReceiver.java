package nl.wolfpackit.appdev12.geofence;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

public class BroadcastReceiver extends android.content.BroadcastReceiver{
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(LocationTrackerService.inRangeNotificationID);
    }
}
