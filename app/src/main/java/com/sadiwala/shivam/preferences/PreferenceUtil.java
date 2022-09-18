package com.sadiwala.shivam.preferences;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.sadiwala.shivam.base.ShivamApplication;

public class PreferenceUtil {

    private static final String TAG = "PreferenceUtil";
    private static String AARYA_PREF = "aarya_pref";

    public static String getStringValue(String key, String defaultValue) {
        SharedPreferences sharedPref = ShivamApplication.getAppContext().getSharedPreferences(AARYA_PREF, MODE_PRIVATE);
        String highScore = sharedPref.getString(key, defaultValue);
        return highScore;
    }

    public static void setStringValue(String key, String value) {
        SharedPreferences sharedPreferences = ShivamApplication.getAppContext().getSharedPreferences(AARYA_PREF, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(key, value);
        myEdit.apply();
    }

    public static long getLongValue(String key, long defaultValue) {
        SharedPreferences sharedPref = ShivamApplication.getAppContext().getSharedPreferences(AARYA_PREF, MODE_PRIVATE);
        long highScore = sharedPref.getLong(key, defaultValue);
        return highScore;
    }

    public static void setLongValue(String key, long value) {
        SharedPreferences sharedPreferences = ShivamApplication.getAppContext().getSharedPreferences(AARYA_PREF, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putLong(key, value);
        myEdit.apply();
    }

}
