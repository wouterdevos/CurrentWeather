package com.wouterdevos.currentweather.valueobject;

import android.content.Context;

import com.wouterdevos.currentweather.R;
import com.wouterdevos.currentweather.CurrentWeatherApplication;
import com.wouterdevos.currentweather.rest.WeatherApiController;

public class Weather {

    private static final String FILENAME_PNG = ".png";

    private String main;
    private String icon;
    private double temp;
    private double tempMin;
    private double tempMax;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        String fullIcon = icon + FILENAME_PNG;
        return String.format(WeatherApiController.IMAGE_URL, fullIcon);
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getFormattedTemp() {
        Context context = CurrentWeatherApplication.getContext();
        return context.getString(R.string.title_temperature, temp);
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public String getFormattedHighAndLow() {
        Context context = CurrentWeatherApplication.getContext();
        return context.getString(R.string.title_high_and_low, tempMax, tempMin);

    }
}
