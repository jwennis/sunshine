package com.example.android.sunshine.util;

import android.content.Context;
import android.text.format.DateUtils;

import com.example.android.sunshine.R;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    public static final long SECOND_MS = 1000;
    public static final long MINUTE_MS = SECOND_MS * 60;
    public static final long HOUR_MS = MINUTE_MS * 60;
    public static final long DAY_MS = HOUR_MS * 24;


    public static long getNormalizedUtcDateToday() {

        long now = System.currentTimeMillis();
        long offset = TimeZone.getDefault().getOffset(now);
        long timeSinceEpochLocal = now + offset;
        long daysSinceEpochLocal = TimeUnit.MILLISECONDS.toDays(timeSinceEpochLocal);

        return TimeUnit.DAYS.toMillis(daysSinceEpochLocal);
    }


    public static long getUTCDateFromLocal(long localDate) {

        return localDate + getGmtOffset(localDate);
    }


    public static long getLocalDateFromUTC(long utcDate) {

        return utcDate - getGmtOffset(utcDate);
    }


    private static long getGmtOffset(long date) {

        return TimeZone.getDefault().getOffset(date);
    }


    public static long normalizeDate(long date) {

        return date / DAY_MS * DAY_MS;
    }


    public static long getDayNumber(long date) {

        return (date + getGmtOffset(date)) / DAY_MS;
    }


    private static String getDayName(Context context, long dateMs) {

        long dayNumber = getDayNumber(dateMs);
        long currentDayNumber = getDayNumber(System.currentTimeMillis());

        if (dayNumber == currentDayNumber) {

            return context.getString(R.string.today);

        } else if (dayNumber == currentDayNumber + 1) {

            return context.getString(R.string.tomorrow);

        } else {

            return new SimpleDateFormat("EEEE").format(dateMs);
        }
    }


    private static String getReadableDateString(Context context, long timeMs) {

        int flags = DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY;

        return DateUtils.formatDateTime(context, timeMs, flags);
    }


    public static String getFriendlyDateString(Context context, long dateMs, boolean showFullDate) {

        long localDate = getLocalDateFromUTC(dateMs);
        long dayNumber = getDayNumber(localDate);
        long currentDayNumber = getDayNumber(System.currentTimeMillis());

        if (dayNumber == currentDayNumber || showFullDate) {

            String dayName = getDayName(context, localDate);
            String readableDate = getReadableDateString(context, localDate);

            if (dayNumber - currentDayNumber < 2) {

                String localizedDayName = new SimpleDateFormat("EEEE").format(localDate);

                return readableDate.replace(localizedDayName, dayName);

            } else {

                return readableDate;
            }

        } else if (dayNumber < currentDayNumber + 7) {

            return getDayName(context, localDate);

        } else {

            int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR
                    | DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_SHOW_WEEKDAY;

            return DateUtils.formatDateTime(context, localDate, flags);
        }
    }
}
