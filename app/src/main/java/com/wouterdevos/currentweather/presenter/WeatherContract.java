package com.wouterdevos.currentweather.presenter;

import com.wouterdevos.currentweather.valueobject.Error;
import com.wouterdevos.currentweather.valueobject.Weather;

public class WeatherContract {

    public interface View {
        void setProgressIndicator(boolean loading);
        void showWeather(Weather weather);
        void showError(Error error);
        void showConnectivityStatus(boolean online);
    }

    public interface Presenter {
        void start();
        void registerEventBus();
        void unregisterEventBus();
        void loadCurrentWeather();
        void findCurrentLocation();
    }
}
