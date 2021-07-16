package com.example.tallie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, String> loadUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put(Constants.NAME, preferences.getString(Constants.NAME, ""));
        userInfo.put(Constants.USERNAME, preferences.getString(Constants.USERNAME, ""));
        userInfo.put(Constants.EMAIL, preferences.getString(Constants.EMAIL, ""));
        userInfo.put(Constants.PHONE, preferences.getString(Constants.PHONE, ""));

        return userInfo;
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

    public static Map<String, String> loadPayment(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        HashMap<String, String> payment = new HashMap<>();
        payment.put(Constants.PAYMENT_CARD_NUMBER, preferences.getString(Constants.PAYMENT_CARD_NUMBER, ""));
        payment.put(Constants.PAYMENT_NAME, preferences.getString(Constants.PAYMENT_NAME, ""));
        payment.put(Constants.PAYMENT_START_DATE, preferences.getString(Constants.PAYMENT_START_DATE, ""));
        payment.put(Constants.PAYMENT_END_DATE, preferences.getString(Constants.PAYMENT_END_DATE, ""));
        payment.put(Constants.PAYMENT_CVC, preferences.getString(Constants.PAYMENT_CVC, ""));

        return payment;
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

    public static void clearData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // clear jwt
        editor.putString(Constants.JWT, "");

        // clear user info
        editor.putString(Constants.NAME, "");
        editor.putString(Constants.USERNAME, "");
        editor.putString(Constants.EMAIL, "");
        editor.putString(Constants.PHONE, "");

        // clear payment
        editor.putString(Constants.PAYMENT_CARD_NUMBER, "");
        editor.putString(Constants.PAYMENT_NAME, "");
        editor.putString(Constants.PAYMENT_START_DATE, "");
        editor.putString(Constants.PAYMENT_END_DATE, "");
        editor.putString(Constants.PAYMENT_CVC, "");
        editor.apply();
    }
}
