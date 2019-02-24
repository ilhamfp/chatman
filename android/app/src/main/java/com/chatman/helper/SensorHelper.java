package com.chatman.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.chatman.model.Chat;

import java.util.Calendar;
import java.util.Date;

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

    private static SensorEventListener initListener(final Context context) {
        return new SensorEventListener() {
            private static final String BOT_TOKEN = "BOT_TOKEN";
            private static final int delay = 10000;

            private long proximityTime = 0;
            private long acceleroTime = 0;

            @Override
            public void onAccuracyChanged(Sensor sensor,  int accuracy){
                //you can leave it empty
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    //proximity
                    float distance = event.values[0];
                    if ((distance <= 5) && (Calendar.getInstance().getTimeInMillis() - proximityTime > delay)) {
                        proximityTime = Calendar.getInstance().getTimeInMillis();
                        BotMessageHelper.sendBotMessage(context, "Don't stare too close to device\n" +
                                "Your distance reached: " + Float.toString(distance) + "cm");
                    }
                }
                else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    //accelero
                    float z = event.values[2];
                    if ((z > -10 && z < -8) && (Calendar.getInstance().getTimeInMillis() - proximityTime > delay)) {
                        proximityTime = Calendar.getInstance().getTimeInMillis();
                        BotMessageHelper.sendBotMessage(context, "Don't use phone while resting down!");
                    }
                }
            }
        };
    }
}
