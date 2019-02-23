package com.chatman.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.chatman.MainActivity;
import com.chatman.R;
import com.chatman.helper.FirebaseHelper;
import com.chatman.helper.PreferencesHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {

    private static final String TAG = PushNotificationService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
        Log.d(TAG, "onMessageReceived: notification body " + remoteMessage.getNotification().getBody());
        sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        PreferencesHelper.setTokenKey(this, s);
        Log.d(TAG, "onNewToken: " + s);
        Log.d(TAG, "onNewToken: " + PreferencesHelper.getUserFirebaseKey(this));
    }

    private void sendNotification(String messageBody, String title) {

        NotificationManager mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (getString(R.string.default_notification_channel_id),
                            getString(R.string.default_notification_channel_name),
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
            notificationChannel.setDescription
                    (getString(R.string.notification_channel_description));
            mNotifyManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(title, messageBody);

        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    private NotificationCompat.Builder getNotificationBuilder(String title, String text) {

        // Set up the pending intent that is delivered when the notification
        // is clicked.
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (this, NOTIFICATION_ID, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification with all of the parameters.
        Drawable drawable= ContextCompat.getDrawable(this,R.mipmap.chatman_launcher_round);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        NotificationCompat.Builder notifyBuilder = new NotificationCompat
                .Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.chatman_launcher_round)
                .setLargeIcon(bitmap)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true).setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }
}
