package com.wouterdevos.currentweather.presenter;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.wouterdevos.currentweather.loader.LocationLoader;
import com.wouterdevos.currentweather.loader.WeatherLoader;
import com.wouterdevos.currentweather.valueobject.Error;
import com.wouterdevos.currentweather.rest.WeatherApiController;
import com.wouterdevos.currentweather.valueobject.Weather;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WeatherPresenter implements WeatherContract.Presenter {

    private static final String TAG = WeatherPresenter.class.getSimpleName();

    public static final int ID_LOCATION_LOADER = 0;
    public static final int ID_WEATHER_LOADER = 1;

    private WeatherContract.View mWeatherView;
    private LoaderManager mLoaderManager;
    private LocationLoader mLocationLoader;
    private WeatherLoader mWeatherLoader;


    private LoaderManager.LoaderCallbacks<Location> mLocationLoaderCallbacks = new LoaderManager.LoaderCallbacks<Location>() {
        @Override
        public Loader<Location> onCreateLoader(int id, Bundle args) {
            return mLocationLoader;
        }

        @Override
        public void onLoadFinished(Loader<Location> loader, Location data) {
            WeatherApiController.getWeatherByCoordinates(data);
        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<Weather> mWeatherLoaderCallbacks = new LoaderManager.LoaderCallbacks<Weather> () {
        @Override
        public Loader<Weather> onCreateLoader(int id, Bundle args) {
            return mWeatherLoader;
        }

        @Override
        public void onLoadFinished(Loader<Weather> loader, Weather weather) {
            if (weather != null) {
                mWeatherView.setProgressIndicator(false);
                mWeatherView.showWeather(weather);
            }
            mLoaderManager.initLoader(ID_LOCATION_LOADER, null, mLocationLoaderCallbacks);
        }

        @Override
        public void onLoaderReset(Loader<Weather> loader) {

        }
    };

    public WeatherPresenter(WeatherContract.View view, LoaderManager loaderManager, Context context) {
        mWeatherView = view;
        mLoaderManager = loaderManager;
        mLocationLoader = new LocationLoader(context);
        mWeatherLoader = new WeatherLoader(context);
    }

    @Override
    public void start() {
//        mLoaderManager.initLoader(ID_LOCATION_LOADER, null, mLocationLoaderCallbacks);
    }

    @Override
    public void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadCurrentWeather() {
        mWeatherView.setProgressIndicator(true);
        mLoaderManager.initLoader(ID_WEATHER_LOADER, null, mWeatherLoaderCallbacks);
    }

    @Override
    public void findCurrentLocation() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRequestFailed(Error error) {
        Log.i(TAG, "onRequestFailed: ");
        mWeatherView.showError(error);
    }
}
