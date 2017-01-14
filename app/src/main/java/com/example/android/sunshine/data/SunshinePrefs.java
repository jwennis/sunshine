package com.example.android.sunshine.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.android.sunshine.R;


public class SunshinePrefs {

    private static final String DEFAULT_WEATHER_LOCATION = "94043,USA";


    public static String getPreferredLocation(Context context) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForLocation = context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_default);

        return prefs.getString(keyForLocation, defaultLocation);
    }


    public static boolean isMetric(Context context) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForUnits = context.getString(R.string.pref_units_key);
        String defaultUnits = context.getString(R.string.pref_units_metric);
        String preferredUnits = prefs.getString(keyForUnits, defaultUnits);
        String metric = context.getString(R.string.pref_units_metric);

        boolean userPrefersMetric;

        if (metric.equals(preferredUnits)) {

            userPrefersMetric = true;

        } else {

            userPrefersMetric = false;
        }

        return userPrefersMetric;
    }
}