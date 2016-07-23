package com.wouterdevos.todaysweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.support.annotation.NonNull;

public class PreferencesHelper {

    private static final String PREFERENCES = "PREFERENCES";

    private static final String LOCATION_KEY = "LOCATION_KEY";
    private static final String LATITUDE_KEY = "LATITUDE_KEY";
    private static final String LONGITUDE_KEY = "LONGITUDE_KEY";


    public static void setLocation(@NonNull Context context, @NonNull Location location) {
        String latitudeString = String.valueOf(location.getLatitude());
        String longitudeString = String.valueOf(location.getLongitude());

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(LATITUDE_KEY, latitudeString);
        editor.putString(LONGITUDE_KEY, longitudeString);
        editor.apply();
    }

    public static Location getLocation(@NonNull Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String latitudeString = preferences.getString(LATITUDE_KEY, null);
        String longitudeString = preferences.getString(LONGITUDE_KEY, null);
        Double latitude = Utils.doubleValueOf(latitudeString);
        Double longitude = Utils.doubleValueOf(longitudeString);

        Location location = null;
        if (!(latitude == null || longitude == null)) {
            location = new Location("");
            location.setLatitude(latitude);
            location.setLongitude(longitude);
        }

        return location;
    }
}