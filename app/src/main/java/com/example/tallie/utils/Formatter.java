package com.example.tallie.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {
    public static String dateToString(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date);
    }
}
