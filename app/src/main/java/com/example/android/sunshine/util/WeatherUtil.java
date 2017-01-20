package com.example.android.sunshine.util;

import android.content.Context;

import com.example.android.sunshine.R;
import com.example.android.sunshine.data.SunshinePrefs;

import java.lang.reflect.Field;

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


    public static String getConditionFromId(Context context, int weatherId) {

        String code;

        if (weatherId >= 200 && weatherId <= 232) {

            code = "2xx";

        } else if (weatherId >= 300 && weatherId <= 321) {

            code = "3xx";

        } else {

            code = String.valueOf(weatherId);
        }

        try {

            Field f = String.class.getDeclaredField("condition_" + code);

            return context.getString(f.getInt(f));

        } catch (Exception e) {

            e.printStackTrace();

            return context.getString(R.string.condition_unknown, weatherId);
        }
    }
}
