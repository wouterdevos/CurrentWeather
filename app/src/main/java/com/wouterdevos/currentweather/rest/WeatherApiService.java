package com.wouterdevos.currentweather.rest;

import com.wouterdevos.currentweather.valueobject.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("weather")
    Call<Weather> getWeatherByCoordinates(@Query("appid") String appId,
                                          @Query("units") String units,
                                          @Query("lat") double lat,
                                          @Query("lon") double lon);

}
