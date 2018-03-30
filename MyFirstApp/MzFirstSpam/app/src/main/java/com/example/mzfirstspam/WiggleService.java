package com.example.mzfirstspam;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;

public class WiggleService extends IntentService {
    static boolean serviceRunning = false;
    private SensorManager mSensorManager;
    private Sensor mSensor2;
    private Sensor mSensor;
    float[] sens2Val = new float[3];
    float[] sensVal = new float[5];

    private final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] x = new float[sensorEvent.values.length];
            x = sensorEvent.values;
            if(sensVal == null){
                sensVal = x.clone();
            } else {
                int count = 0;
                Log.d("sensro1", Float.toString(x[0]));
                Log.d("sensro1", Float.toString(sensVal[0]));
                for(int i = 0; i < x.length; i++){
                     if (x[i]>= sensVal[i]* 2 || x[i] <= 0.5 * sensVal[i]){
                               count++;
                    }
                }
                if ( count >= x.length){
                    sendNotification();
                }
                sensVal = x.clone();
            }
        }
        public void sendNotification(){
            String CHANNEL_ID = "200A";
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Are you okay.")
                    .setPriority(NotificationCompat.PRIORITY_MIN);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(4, mBuilder.build());
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private final SensorEventListener mListener2 = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] x = new float[sensorEvent.values.length];
            Log.d("sensro2", Float.toString(sens2Val[0]));
            x = sensorEvent.values;
            if (sens2Val == null) {
                sens2Val = x.clone();
            } else {
                int count = 0;
                Log.d("sensro2", Float.toString(x[0]));

                for (int i = 0; i < x.length; i++) {
                    if (x[i] >= sens2Val[i] * 2 || x[i] <= sens2Val[i] * 0.5) {
                        count++;
                    }
                    sens2Val = x.clone();
                }
                if ( count > x.length){
                    sendNotification();
                }
            }
        }

        public void sendNotification(){
            String CHANNEL_ID = "200A";
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Are you okay.")
                    .setPriority(NotificationCompat.PRIORITY_MIN);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(4, mBuilder.build());
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public WiggleService() {
        super("WiggleService");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("Wiggle", "service reached");
        serviceRunning = true;
        Log.d("wiggle", Boolean.toString(serviceRunning));
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(mSensor == null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }
        if(mSensor2 == null) {
            mSensor2 = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }

        mSensorManager.registerListener((SensorEventListener) mListener,mSensor, SensorManager.SENSOR_DELAY_NORMAL );
        mSensorManager.registerListener((SensorEventListener) mListener2,mSensor2, SensorManager.SENSOR_DELAY_NORMAL );
}

    protected static boolean getRunning(){
        return serviceRunning;
    }
}
