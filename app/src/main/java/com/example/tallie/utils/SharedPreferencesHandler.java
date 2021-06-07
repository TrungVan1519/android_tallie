package com.example.tallie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

public class SharedPreferencesHandler {

    private final static String FILENAME = "tallie";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        String username = preferences.getString(USERNAME, "");
        String password = preferences.getString(PASSWORD, "");

        return !username.isEmpty() && !password.isEmpty();
    }

    public static List<String> loadAppData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        String username = preferences.getString(USERNAME, "");
        String password = preferences.getString(PASSWORD, "");

        return Arrays.asList(username, password);
    }

    public static void saveAppData(Context context, String username, String password) {
        SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.apply();
    }
}
