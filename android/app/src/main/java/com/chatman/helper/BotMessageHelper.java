package com.chatman.helper;

import android.content.Context;
import android.content.Intent;

import com.chatman.activity.CompassActivity;
import com.chatman.model.Chat;

import java.util.Calendar;

public class BotMessageHelper {
    private static final String BOT_TOKEN = "BOT_TOKEN";

    public static void respondBot(Context context, String message) {
        if (message.equals("compass")) {
            Intent intent = new Intent(context, CompassActivity.class);
            context.startActivity(intent);
        }
        else sendBotMessage(context, "Halo! Selamat datang di ChatMan");
    }

    public static void sendBotMessage(Context context, String message) {
        Chat botMessage = new Chat(BOT_TOKEN, "ChatMan Bot", PreferencesHelper.getToken(context), Calendar.getInstance().getTime(), message);
        String botKey = FirebaseHelper.dbMessage.push().getKey();
        FirebaseHelper.dbMessage.child(botKey).setValue(botMessage);
    }
}
