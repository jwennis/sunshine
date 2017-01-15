package com.example.android.sunshine.util;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.SunshinePrefs;
import com.example.android.sunshine.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;


public class JsonUtil {

    private static final String OWM_LIST = "list";
    private static final String OWM_CITY = "city";
    private static final String OWM_COORD = "coord";
    private static final String OWM_LATITUDE = "lat";
    private static final String OWM_LONGITUDE = "lon";
    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WINDSPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "deg";

    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_ID = "id";

    private static final String OWM_TEMPERATURE = "temp";
    private static final String OWM_MAX = "max";
    private static final String OWM_MIN = "min";

    private static final String OWM_MESSAGE_CODE = "cod";

    public static String[] parseWeatherStrings(Context context, String jsonForecast)
            throws JSONException {

        final String OWM_LIST = "list";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";
        final String OWM_MESSAGE_CODE = "cod";

        String[] parsedWeatherData = null;

        JSONObject forecastJson = new JSONObject(jsonForecast);

        if (forecastJson.has(OWM_MESSAGE_CODE)) {

            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {

                case HttpURLConnection.HTTP_OK: {

                    break;
                }

                case HttpURLConnection.HTTP_NOT_FOUND: {

                    return null;
                }

                default: {

                    return null;
                }
            }
        }

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        parsedWeatherData = new String[weatherArray.length()];

        long localDate = System.currentTimeMillis();
        long utcDate = DateUtil.getUTCDateFromLocal(localDate);
        long startDay = DateUtil.normalizeDate(utcDate);

        for (int i = 0; i < weatherArray.length(); i++) {

            String date;
            String highAndLow;

            long dateTimeMillis;
            double high;
            double low;
            String description;

            JSONObject dayForecast = weatherArray.getJSONObject(i);

            dateTimeMillis = startDay + DateUtil.DAY_MS * i;
            date = DateUtil.getFriendlyDateString(context, dateTimeMillis, false);

            JSONObject weatherObject =
                    dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);

            description = weatherObject.getString(OWM_DESCRIPTION);

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            high = temperatureObject.getDouble(OWM_MAX);
            low = temperatureObject.getDouble(OWM_MIN);
            highAndLow = WeatherUtil.formatHighLows(context, high, low);

            parsedWeatherData[i] = date + " - " + description + " - " + highAndLow;
        }

        return parsedWeatherData;
    }


    public static ContentValues[] getWeatherValues(Context context, String json)
            throws JSONException {

        JSONObject forecastJson = new JSONObject(json);

        if (forecastJson.has(OWM_MESSAGE_CODE)) {

            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {

                case HttpURLConnection.HTTP_OK: {

                    break;
                }

                case HttpURLConnection.HTTP_NOT_FOUND: {

                    return null;
                }

                default: {

                    return null;
                }
            }
        }

        JSONArray jsonWeatherArray = forecastJson.getJSONArray(OWM_LIST);
        JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
        JSONObject cityCoord = cityJson.getJSONObject(OWM_COORD);
        double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
        double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);

        SunshinePrefs.setLocationDetails(context, cityLatitude, cityLongitude);
        ContentValues[] weatherContentValues = new ContentValues[jsonWeatherArray.length()];

        long normalizedUtcStartDay = DateUtil.getNormalizedUtcDateToday();

        for (int i = 0; i < jsonWeatherArray.length(); i++) {

            long dateTimeMillis;
            double pressure;
            int humidity;
            double windSpeed;
            double windDirection;
            double high;
            double low;
            int weatherId;

            JSONObject dayForecast = jsonWeatherArray.getJSONObject(i);
            dateTimeMillis = normalizedUtcStartDay + DateUtil.DAY_MS * i;
            pressure = dayForecast.getDouble(OWM_PRESSURE);
            humidity = dayForecast.getInt(OWM_HUMIDITY);
            windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
            windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            weatherId = weatherObject.getInt(OWM_WEATHER_ID);

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            high = temperatureObject.getDouble(OWM_MAX);
            low = temperatureObject.getDouble(OWM_MIN);

            ContentValues weatherValues = new ContentValues();
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTimeMillis);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, high);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, low);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherId);
            weatherContentValues[i] = weatherValues;
        }

        return weatherContentValues;
    }
}
