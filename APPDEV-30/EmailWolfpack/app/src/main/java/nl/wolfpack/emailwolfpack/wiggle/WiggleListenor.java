package nl.wolfpack.emailwolfpack.wiggle;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

public class WiggleListenor implements SensorEventListener {
    private SensorManager mSensorMgr;
    private final Sensor mAccelerometer;
    private Context mContext;
    private OnShakeListener mShakeListener;


    private int mForceTreshhold = 350;
    private int mTimeTreshhold = 100;
    private int mShakeTimeout = 500;
    private int mShakeDurating = 1000;
    private int mShakeCount = 4;

    private float mLastX=-1.0f, mLastY=-1.0f, mLastZ=-1.0f;
    private long mLastTime;
    private long mLastShake;
    private long mLastForce;



    public interface OnShakeListener
    {
        public void onShake();
    }


    public WiggleListenor(Context context) {
        mContext = context;
        mSensorMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        resume();
    }

    public void setOnShakeListener(OnShakeListener listener)
    {
        mShakeListener = listener;
    }

    public void resume() {
        if (mSensorMgr == null) {
            throw new UnsupportedOperationException("Sensors not supported");
        }
        boolean supported = mSensorMgr.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        if (!supported) {
            mSensorMgr.unregisterListener(this, mAccelerometer);
            throw new UnsupportedOperationException("Accelerometer not supported");
        }
    }

    public void pause() {
        if (mSensorMgr != null) {
            mSensorMgr.unregisterListener(this, mAccelerometer);
            mSensorMgr = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (mShakeListener == null) return;
        long now = System.currentTimeMillis();

        if ((now - mLastForce) > mShakeTimeout) {
            mShakeCount = 0;
        }

        if ((now - mLastTime) > mTimeTreshhold) {
            long diff = now - mLastTime;
            float speed = Math.abs(sensorEvent.values[0] + sensorEvent.values[1] + sensorEvent.values[2] - mLastX - mLastY - mLastZ) / diff * 10000;
            if (speed > mForceTreshhold) {
                if ((++mShakeCount >= mShakeCount) && (now - mLastShake > mShakeDurating)) {
                    mLastShake = now;
                    mShakeCount = 0;
                    if (mShakeListener != null) {
                        mShakeListener.onShake();
                    }
                }
                mLastForce = now;
            }
            mLastTime = now;
            mLastX = sensorEvent.values[0];
            mLastY = sensorEvent.values[1];
            mLastZ = sensorEvent.values[2];
        }
    }

    public void setmForceTreshhold(int mForceTreshhold) {
        this.mForceTreshhold = mForceTreshhold;
    }

    public void setmTimeTreshhold(int mTimeTreshhold) {
        this.mTimeTreshhold = mTimeTreshhold;
    }

    public void setmShakeTimeout(int mShakeTimeout) {
        this.mShakeTimeout = mShakeTimeout;
    }

    public void setmShakeDurating(int mShakeDurating) {
        this.mShakeDurating = mShakeDurating;
    }

    public void setmShakeCount(int mShakeCount) {
        this.mShakeCount = mShakeCount;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
