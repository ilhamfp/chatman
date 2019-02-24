package com.chatman.activity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.chatman.R;

public class CompassActivity extends Activity implements SensorEventListener {

    private ImageView mCompass;
    private TextView mHeading;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] latestAccelerometer = new float[3];
    private float[] latestMagnetometer = new float[3];
    private boolean mLastAccelerometerUpdated = false;
    private boolean mLastMagnetometerUpdated = false;
    private float[] mRotation = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mCompass = findViewById(R.id.compass);
        mHeading = findViewById(R.id.tvHeading);

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, latestAccelerometer, 0, event.values.length);
            mLastAccelerometerUpdated = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, latestMagnetometer, 0, event.values.length);
            mLastMagnetometerUpdated = true;
        }
        if (mLastAccelerometerUpdated && mLastMagnetometerUpdated) {
            SensorManager.getRotationMatrix(mRotation, null, latestAccelerometer, latestMagnetometer);
            SensorManager.getOrientation(mRotation, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);

            ra.setFillAfter(true);

            mCompass.startAnimation(ra);
            mCurrentDegree = -azimuthInDegress;
            mHeading.setText("Heading: " + Float.toString(azimuthInDegress) + " degrees");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

}