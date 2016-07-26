package com.wouterdevos.currentweather.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.wouterdevos.currentweather.valueobject.Weather;

import java.lang.reflect.Type;

public class WeatherDeserializer implements JsonDeserializer<Weather> {

    private static final String TAG = WeatherDeserializer.class.getSimpleName();

    @Override
    public Weather deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        try {
            Weather weather = new Weather();
            JsonArray weatherArray = json.getAsJsonObject().get("weather").getAsJsonArray();
            JsonElement weatherItem = weatherArray.get(0);
            JsonElement mainObject = json.getAsJsonObject().get("main");

            weather.setMain(weatherItem.getAsJsonObject().get("main").getAsString());
            weather.setIcon(weatherItem.getAsJsonObject().get("icon").getAsString());
            weather.setTemp(mainObject.getAsJsonObject().get("temp").getAsDouble());
            weather.setTempMin(mainObject.getAsJsonObject().get("temp_min").getAsDouble());
            weather.setTempMax(mainObject.getAsJsonObject().get("temp_max").getAsDouble());

            return weather;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
