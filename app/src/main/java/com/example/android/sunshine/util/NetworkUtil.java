package com.example.android.sunshine.util;

import android.net.Uri;

import com.example.android.sunshine.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil {

    private static final String FORMAT = "json";
    private static final String UNITS = "metric";
    private static final int NUM_DAYS = 14;

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";
    private static final String PARAM_QUERY = "q";
    private static final String PARAM_LAT = "lat";
    private static final String PARAM_LON = "lon";
    private static final String PARAM_FORMAT = "mode";
    private static final String PARAM_UNITS = "units";
    private static final String PARAM_DAYS = "cnt";
    private static final String PARAM_API_KEY = "appid";


    public static URL buildUrl(String locationQuery) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, locationQuery)
                .appendQueryParameter(PARAM_FORMAT, FORMAT)
                .appendQueryParameter(PARAM_UNITS, UNITS)
                .appendQueryParameter(PARAM_DAYS, Integer.toString(NUM_DAYS))
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .build();

        URL url = null;

        try {

            url = new URL(builtUri.toString());

        } catch (MalformedURLException e) {

            e.printStackTrace();
        }

        return url;
    }


    public static String getHttpResponse(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            return scanner.hasNext() ? scanner.next() : null;

        } finally {

            urlConnection.disconnect();
        }
    }
}
