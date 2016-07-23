package com.wouterdevos.todaysweather.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class WeatherDeserializer implements JsonDeserializer<WeatherResponse> {

    private static final String TAG = WeatherDeserializer.class.getSimpleName();

    @Override
    public WeatherResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        try {
            WeatherResponse weatherResponse = new WeatherResponse();
            JsonArray weatherArray = json.getAsJsonObject().get("weather").getAsJsonArray();
            JsonElement weatherItem = weatherArray.get(0);
            JsonElement mainObject = json.getAsJsonObject().get("main");

            weatherResponse.setMain(weatherItem.getAsJsonObject().get("main").getAsString());
            weatherResponse.setIcon(weatherItem.getAsJsonObject().get("icon").getAsString());
            weatherResponse.setHumidity(mainObject.getAsJsonObject().get("humidity").getAsDouble());
            weatherResponse.setTemp(mainObject.getAsJsonObject().get("temp").getAsDouble());
            weatherResponse.setTempMin(mainObject.getAsJsonObject().get("temp_min").getAsDouble());
            weatherResponse.setTempMax(mainObject.getAsJsonObject().get("temp_max").getAsDouble());

            return weatherResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
