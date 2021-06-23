package com.example.tallie.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHandler {

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        return !preferences.getString(Constants.JWT, "").isEmpty();
    }

    public static String loadAppData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        return preferences.getString(Constants.JWT, "");
    }

    public static void saveAppData(Context context, String jwt) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.JWT, jwt);
        editor.apply();
    }
}
