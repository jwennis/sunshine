package com.example.android.sunshine.data;

import android.content.Context;


public class SunshinePrefs {

    private static final String DEFAULT_WEATHER_LOCATION = "94043,USA";


    public static String getPreferredLocation(Context context) {

        return DEFAULT_WEATHER_LOCATION;
    }


    public static boolean isMetric(Context context) {

        return true;
    }
}