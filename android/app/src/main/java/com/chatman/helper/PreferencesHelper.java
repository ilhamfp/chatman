package com.chatman.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesHelper {

    public static final String USER_FIREBASE_KEY = "user_firebase_key";
    public static final String USER_NAME = "user_name";

    private static void setBooleanPreferences(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static boolean getBooleanPreferences(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }

    private static void setStringPreferences(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getStringPreferences(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

    private static void setIntPreferences(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static int getIntPreferences(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, 0);
    }

    public static void setUserFirebaseKey(Context context, String value) {
        setStringPreferences(context, USER_FIREBASE_KEY, value);
    }

    public static String getUserFirebaseKey(Context context) {
        return getStringPreferences(context, USER_FIREBASE_KEY);
    }

    public static void setUserName(Context context, String value) {
        setStringPreferences(context, USER_NAME, value);
    }

    public static String getUserName(Context context) {
        return getStringPreferences(context, USER_NAME);
    }


}
