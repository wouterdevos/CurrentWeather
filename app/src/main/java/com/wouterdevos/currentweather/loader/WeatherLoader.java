package com.wouterdevos.currentweather.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.wouterdevos.currentweather.database.DatabaseHelper;
import com.wouterdevos.currentweather.valueobject.Weather;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WeatherLoader extends AsyncTaskLoader<Weather> {

    private static final String TAG = WeatherLoader.class.getSimpleName();

    private Weather mWeather;

    public WeatherLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        EventBus.getDefault().register(this);

        if (mWeather != null) {
            deliverResult(mWeather);
        }

        if (takeContentChanged() || mWeather == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    public Weather loadInBackground() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.readWeather();
    }

    @Override
    public void deliverResult(Weather data) {
        mWeather = data;
        super.deliverResult(data);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRequestSuccess(Weather weather) {
        Log.i(TAG, "onRequestSuccess: ");
        mWeather = weather;
        onContentChanged();
    }

}
