package com.chatman.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class SensorHelper {
    public static final String TAG = SensorHelper.class.getSimpleName();

    private Context mContext;
    private SensorManager sensorManager;
    private SensorEventListener sensorListener;

    public SensorHelper(Context context) {
        this.mContext = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorListener = initListener(mContext);
    }

    public void listenSensor(int sensorType) {
        if (sensorManager.getDefaultSensor(sensorType) != null) {
            sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(sensorType), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unlistenSensor() {
        sensorManager.unregisterListener(sensorListener);
    }

    public static SensorEventListener initListener(final Context context) {
        return new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor,  int accuracy){
                //you can leave it empty
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    //proximity
                    float distance = event.values[0];
                    if (distance <= 5) {
                        Toast.makeText(context, "You are too close to device", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    //accelero
                    float z = event.values[2];
                    if (z > -10 && z < -8) {
                        Toast.makeText(context, "Please don't use phone while resting down", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }
}
