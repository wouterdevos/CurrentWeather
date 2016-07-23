package com.wouterdevos.todaysweather.rest;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiController {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String IMAGE_URL = "http://openweathermap.org/img/w/";
    private static final String APP_ID = "257103d5c2fd659a0281deda32fe6014";
    private static final String UNITS_METRIC = "metric";

    private static final int UNKOWN_STATUS_CODE = -1;

    private static Retrofit sRetrofit;

    private static Retrofit getRetrofitInstance() {
        if (sRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(WeatherResponse.class, new WeatherDeserializer())
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
        Call<WeatherResponse> call = apiService.getWeatherByCoordinates(APP_ID, UNITS_METRIC,
                location.getLatitude(), location.getLongitude());
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
//                        EventBus.getDefault().post(response.body());
                    } else {
                        postRequestFailed(UNKOWN_STATUS_CODE);
                    }
                } else {
                    postRequestFailed(response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                postRequestFailed(UNKOWN_STATUS_CODE);
            }
        });
    }

    private static void postRequestFailed(int statusCode) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(statusCode);
//        EventBus.getDefault().post(errorResponse);
    }
}
