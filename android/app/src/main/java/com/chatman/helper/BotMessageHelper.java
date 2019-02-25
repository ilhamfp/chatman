package com.chatman.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.chatman.activity.CompassActivity;
import com.chatman.model.Chat;
import com.chatman.service.WeatherService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class BotMessageHelper {
    private static final String BOT_TOKEN = "BOT_TOKEN";

    public static void respondBot(Context context, String message) {
        if (message.equals("compass")) {
            Intent intent = new Intent(context, CompassActivity.class);
            context.startActivity(intent);
        }
        else if (message.equals("weather today")) {
            GpsHelper gps = new GpsHelper(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                sendBotMessage(context, "GPS not allowed");
            }
            else {
                new WeatherService(context).execute(gps.getLocation());
            }
        }
        else sendBotMessage(context, "Halo! Selamat datang di ChatMan");
    }

    public static void sendBotMessage(final Context context, String message) {
        Chat botMessage = new Chat(BOT_TOKEN, "ChatMan Bot", PreferencesHelper.getToken(context), Calendar.getInstance().getTime(), message);
        final String botKey = FirebaseHelper.dbMessage.push().getKey();
        FirebaseHelper.dbMessage.child(botKey).setValue(botMessage);

        FirebaseDatabase.getInstance().getReference().child("chatroom").child(PreferencesHelper.getBotRoom(context)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String index = String.valueOf(dataSnapshot.child("messages").getChildrenCount());
                Log.d("BotMessageHelper", "onDataChange: index " + index);
                FirebaseDatabase.getInstance().getReference("chatroom/" + PreferencesHelper.getBotRoom(context) + "/messages")
                        .child(index).setValue(botKey);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
