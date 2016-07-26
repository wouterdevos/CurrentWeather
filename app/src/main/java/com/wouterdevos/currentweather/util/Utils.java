package com.wouterdevos.currentweather.util;

import android.util.Log;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static Double doubleValueOf(String string) {
        Double value = null;
        try {
            value = Double.valueOf(string);
        } catch (NumberFormatException e) {
            Log.i(TAG, e.toString());
        }

        return value;
    }
}
