package com.wouterdevos.currentweather.presenter;

import com.wouterdevos.currentweather.valueobject.Error;
import com.wouterdevos.currentweather.valueobject.Weather;

public class WeatherContract {

    public interface View {
        void setProgressIndicator(boolean loading);
        void showWeather(Weather weather);
        void showWeatherError(Error error);
        void showLocationError();
    }

    public interface Presenter {
        void startWeatherLoader();
        void startLocationLoader();
        void registerEventBus();
        void unregisterEventBus();
    }
}
