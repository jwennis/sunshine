package com.example.android.sunshine.util;

import android.content.Context;

import com.example.android.sunshine.R;
import com.example.android.sunshine.data.SunshinePrefs;

public class WeatherUtil {


    public static String formatHighLows(Context context, double high, double low) {

        long hRound = Math.round(high);
        long lRound = Math.round(low);

        String hFormat = formatTemp(context, hRound);
        String lFormat = formatTemp(context, lRound);

        return String.format("%s / %s", hFormat, lFormat);
    }


    public static String formatTemp(Context context, double temperature) {

        int tempFormatResId = R.string.format_temp_celsius;

        if (!SunshinePrefs.isMetric(context)) {

            temperature = toFahrenheit(temperature);
            tempFormatResId = R.string.format_temp_fahrenheit;
        }

        return String.format(context.getString(tempFormatResId), temperature);
    }


    private static double toFahrenheit(double celsius) {

        return (celsius * 1.8) + 32;
    }
}
