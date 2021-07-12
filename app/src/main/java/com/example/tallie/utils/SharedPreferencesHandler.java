package com.example.tallie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

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

    public static List<String> loadUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        return Arrays.asList(
                preferences.getString(Constants.NAME, ""),
                preferences.getString(Constants.USERNAME, ""),
                preferences.getString(Constants.EMAIL, ""),
                preferences.getString(Constants.PHONE, ""));
    }

    public static void saveUserInfo(Context context, String name, String username, String email, String phone) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.NAME, name);
        editor.putString(Constants.USERNAME, username);
        editor.putString(Constants.EMAIL, email);
        editor.putString(Constants.PHONE, phone);
        editor.apply();
    }

    public static List<String> loadPayment(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        return Arrays.asList(
                preferences.getString(Constants.PAYMENT_CARD_NUMBER, ""),
                preferences.getString(Constants.PAYMENT_NAME, ""),
                preferences.getString(Constants.PAYMENT_START_DATE, ""),
                preferences.getString(Constants.PAYMENT_END_DATE, ""),
                preferences.getString(Constants.PAYMENT_CVC, ""));
    }

    public static void savePayment(Context context, String paymentCardNumber, String paymentName, String paymentStartDate, String paymentEndDate, String paymentCVC) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PAYMENT_CARD_NUMBER, paymentCardNumber);
        editor.putString(Constants.PAYMENT_NAME, paymentName);
        editor.putString(Constants.PAYMENT_START_DATE, paymentStartDate);
        editor.putString(Constants.PAYMENT_END_DATE, paymentEndDate);
        editor.putString(Constants.PAYMENT_CVC, paymentCVC);
        editor.apply();
    }
}
