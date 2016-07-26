package com.wouterdevos.currentweather.rest;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wouterdevos.currentweather.database.DatabaseHelper;
import com.wouterdevos.currentweather.valueobject.Error;
import com.wouterdevos.currentweather.valueobject.Weather;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiController {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String APP_ID = "257103d5c2fd659a0281deda32fe6014";
    private static final String UNITS_METRIC = "metric";

    private static final int UNKOWN_STATUS_CODE = -1;

    public static final String IMAGE_URL = "http://openweathermap.org/img/w/%s";

    private static Retrofit sRetrofit;

    private static Retrofit getRetrofitInstance() {
        if (sRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Weather.class, new WeatherDeserializer())
                    .create();

            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return sRetrofit;
    }

    public static void getWeatherByCoordinates(Location location) {
        Retrofit retrofit = getRetrofitInstance();
        WeatherApiService apiService = retrofit.create(WeatherApiService.class);
        Call<Weather> call = apiService.getWeatherByCoordinates(APP_ID, UNITS_METRIC,
                location.getLatitude(), location.getLongitude());
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    Weather weather = response.body();
                    if (weather != null) {
                        DatabaseHelper.getInstance().insertWeather(weather);
                        EventBus.getDefault().post(weather);
                    } else {
                        postRequestFailed(UNKOWN_STATUS_CODE);
                    }
                } else {
                    postRequestFailed(response.code());
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                postRequestFailed(UNKOWN_STATUS_CODE);
            }
        });
    }

    private static void postRequestFailed(int statusCode) {
        Error error = new Error();
        error.setStatusCode(statusCode);
        EventBus.getDefault().post(error);
    }
}
