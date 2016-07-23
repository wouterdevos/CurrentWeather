package com.wouterdevos.todaysweather.model;

import android.location.Location;

import com.wouterdevos.todaysweather.rest.WeatherApiController;

public class WeatherModel {

    private static WeatherModel sInstance;

    private WeatherModel() {

    }

    public static WeatherModel getInstance() {
        if (sInstance == null) {
            sInstance = new WeatherModel();
        }

        return sInstance;
    }

    public void getWeatherByCoordinates(Location location) {
        WeatherApiController.getWeatherByCoordinates(location);
    }
}
