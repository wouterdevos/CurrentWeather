package com.wouterdevos.currentweather.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wouterdevos.currentweather.valueobject.Weather;

public class WeatherTable {

    public static final String TABLE_NAME = "current_weather";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MAIN = "main";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_TEMP = "temp";
    public static final String COLUMN_TEMP_MIN = "temp_min";
    public static final String COLUMN_TEMP_MAX = "temp_max";

    public static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MAIN + " TEXT, "
            + COLUMN_ICON + " TEXT, "
            + COLUMN_TEMP + " REAL, "
            + COLUMN_TEMP_MIN + " REAL, "
            + COLUMN_TEMP_MAX + " REAL"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public static void insertWeather(SQLiteDatabase database, Weather weather) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID, 0);
        contentValues.put(COLUMN_MAIN, weather.getMain());
        contentValues.put(COLUMN_ICON, weather.getIcon());
        contentValues.put(COLUMN_TEMP, weather.getTemp());
        contentValues.put(COLUMN_TEMP_MIN, weather.getTempMin());
        contentValues.put(COLUMN_TEMP_MAX, weather.getTempMax());

        database.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static Weather readWeather(SQLiteDatabase database) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=" + 0;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor == null || cursor.getCount() < 1) {
            return null;
        }

        Weather weather = new Weather();
        if (cursor.moveToFirst()) {
            do {
                weather = getWeatherFromCursor(cursor);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return weather;
    }

    private static Weather getWeatherFromCursor(Cursor cursor) {
        // Start at 1 to skip the id.
        int i = 1;
        Weather weather = new Weather();
        weather.setMain(cursor.getString(i++));
        weather.setIcon(cursor.getString(i++));
        weather.setTemp(cursor.getDouble(i++));
        weather.setTempMin(cursor.getDouble(i++));
        weather.setTempMax(cursor.getDouble(i++));

        return weather;
    }
}
