package com.example.android.sunshine.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;


public class JsonUtil {


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

        //...

        return parsedWeatherData;
    }
}
