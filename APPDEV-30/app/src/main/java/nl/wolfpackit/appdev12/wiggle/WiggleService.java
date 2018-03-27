package nl.wolfpackit.appdev12.wiggle;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import java.text.DecimalFormat;
import java.util.Arrays;

import nl.wolfpackit.appdev12.MainActivity;
import nl.wolfpackit.appdev12.R;
import nl.wolfpackit.appdev12.geofence.BroadcastReceiver;

public class WiggleService extends Service{
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TriggerEventListener mTriggerEventListener;
    private float[] gravity = new float[]{0,0,0};

    private BroadcastReceiver receiver;

    private float shakeThreshold = 3;
    private int timeRange = 2000;
    private long startTime = 0;
    private int requiredShakes = 4;
    private int shakesLeft = 0;
    private boolean measureUp = false;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        startForeground(10, new NotificationCompat.Builder(getApplication(), getString(R.string.channel_name))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.wiggleServiceNotificationTitle))
                .setContentText(getString(R.string.wiggleServiceNotificationText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build());
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            mSensorManager.registerListener(new SensorEventListener(){
                public void onSensorChanged(SensorEvent event) {
                    // In this example, alpha is calculated as t / (t + dT),
                    // where t is the low-pass filter's time-constant and
                    // dT is the event delivery rate.
                    final float alpha = 0.8f;

                    // Isolate the force of gravity with the low-pass filter.
                    gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                    gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                    gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                    // Remove the gravity contribution with the high-pass filter.
                    float[] linear_acceleration = new float[3];
                    linear_acceleration[0] = event.values[0] - gravity[0];
                    linear_acceleration[1] = event.values[1] - gravity[1];
                    linear_acceleration[2] = event.values[2] - gravity[2];

                    //test if the shake is quick enough
                    long time = System.currentTimeMillis();
                    if(time>startTime+timeRange){
                        shakesLeft = requiredShakes;
                        startTime = time;
                    }

                    //check if we have detected enough shakes
                    if(linear_acceleration[1]>shakeThreshold && measureUp){
                        measureUp = false;
                        shakesLeft--;
                    }else if(linear_acceleration[1]<-shakeThreshold && !measureUp){
                        measureUp = true;
                        shakesLeft--;
                    }

                    //if there are no shakes left, send a notification
                    if(shakesLeft==0){
                        createNotification();
                    }
                }

                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            }, mSensor, SensorManager.SENSOR_DELAY_UI);
        }


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals("nl.wolfpack.UPDATE_WIGGLE_DATA")){
                    requiredShakes = intent.getIntExtra("requiredShakes", requiredShakes);
                    shakeThreshold = intent.getFloatExtra("shakeThreshold", shakeThreshold);
                    timeRange = intent.getIntExtra("timeRange", timeRange);
                    shakesLeft = requiredShakes;
//                    System.out.println(requiredShakes+"-"+shakeThreshold+"-"+timeRange);
                }
            }
        };
        IntentFilter filter = new IntentFilter("nl.wolfpack.UPDATE_WIGGLE_DATA");
        registerReceiver(receiver, filter);

        return START_STICKY;
    }

    protected void createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Application application = getApplication();

            String name = application.getString(R.string.channel_name);
            String channelID = (String)name;

            //create notification
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(application, channelID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(application.getString(R.string.wiggleNotificationTitle))
                    .setContentText(application.getString(R.string.wiggleNotificationText))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Register the channel with the system
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(application);
            Notification notification = mBuilder.build();
            notificationManager.notify(34, notification);
        }
    }
}
