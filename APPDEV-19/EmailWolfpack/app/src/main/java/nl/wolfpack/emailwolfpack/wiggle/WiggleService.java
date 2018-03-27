package nl.wolfpack.emailwolfpack.wiggle;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class WiggleService extends Service implements WiggleListenor.OnShakeListener{
    private final String TAG = "WiggleService";

    private WiggleListenor mWiggle;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = ((SensorManager)getSystemService(Context.SENSOR_SERVICE));
        mAccelerometer = this.mSensorManager.getDefaultSensor(1);
        mWiggle = new WiggleListenor(this);
        mWiggle.setOnShakeListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onShake() {
        Log.d(TAG, "onShake");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}
