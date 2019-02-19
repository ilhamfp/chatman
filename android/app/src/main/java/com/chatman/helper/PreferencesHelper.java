package com.etsi.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesHelper {

    private static final String USER_ROLE_KEY = "user_role_key";
    public static final boolean USER_ROLE_KREATOR = true;
    public static final boolean USER_ROLE_SOBAT = false;
    public static final String USER_FIREBASE_KEY = "user_firebase_key";
    public static final String KREATOR1_TIER_KEY = "kreator1_tier_key";
    public static final String KREATOR2_TIER_KEY = "kreator2_tier_key";
    public static String CURRENT_KREATOR_KEY;

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

    public static void setUserRole(Context context, boolean value) {
        setBooleanPreferences(context, USER_ROLE_KEY, value);
    }

    public static boolean getUserRole(Context context) {
        return getBooleanPreferences(context, USER_ROLE_KEY);
    }

    public static void setUserFirebaseKey(Context context, String value) {
        setStringPreferences(context, USER_FIREBASE_KEY, value);
    }

    public static String getUserFirebaseKey(Context context) {
        return getStringPreferences(context, USER_FIREBASE_KEY);
    }

    public static void setKreator1TierKey(Context context, int value){
        setIntPreferences(context, KREATOR1_TIER_KEY, value);
    }

    public static int getKreator1TierKey(Context context){
        return getIntPreferences(context, KREATOR1_TIER_KEY);
    }

    public static void setKreator2TierKey(Context context, int value){
        setIntPreferences(context, KREATOR2_TIER_KEY, value);
    }

    public static int getKreator2TierKey(Context context){
        return getIntPreferences(context, KREATOR2_TIER_KEY);
    }


    public static String getCurrentKreatorKey() {
        return CURRENT_KREATOR_KEY;
    }

    public static void setCurrentKreatorKey(String value) {
       CURRENT_KREATOR_KEY = value;
    }
}
