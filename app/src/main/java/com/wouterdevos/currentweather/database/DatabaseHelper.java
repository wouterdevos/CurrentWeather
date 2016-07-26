package com.wouterdevos.currentweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wouterdevos.currentweather.CurrentWeatherApplication;
import com.wouterdevos.currentweather.valueobject.Weather;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper sInstance;

    public static DatabaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(CurrentWeatherApplication.getContext());
        }

        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        WeatherTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        WeatherTable.onUpgrade(database, oldVersion, newVersion);
    }

    public void insertWeather(Weather weather) {
        SQLiteDatabase database = getReadableDatabase();
        WeatherTable.insertWeather(database, weather);
        database.beginTransaction();
        try {
            WeatherTable.insertWeather(database, weather);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public Weather readWeather() {
        SQLiteDatabase database = getReadableDatabase();
        Weather weather = WeatherTable.readWeather(database);
        return weather;
    }
}
