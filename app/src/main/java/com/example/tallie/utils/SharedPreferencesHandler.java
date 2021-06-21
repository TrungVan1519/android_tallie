package com.example.tallie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

public class SharedPreferencesHandler {

    private final static String FILENAME = "tallie";
    private final static String JWT = "jwt";

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return !preferences.getString(JWT, "").isEmpty();
    }

    public static String loadAppData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return preferences.getString(JWT, "");
    }

    public static void saveAppData(Context context, String jwt) {
        SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(JWT, jwt);
        editor.apply();
    }
}
