package com.chatman.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.IBinder;

import com.chatman.helper.SensorHelper;

public class DetectionService extends Service {
    private SensorHelper sensorHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        sensorHelper = new SensorHelper(this);
        sensorHelper.listenSensor(Sensor.TYPE_PROXIMITY);
        sensorHelper.listenSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
