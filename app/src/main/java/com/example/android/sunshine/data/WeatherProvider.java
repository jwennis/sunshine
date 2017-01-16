package com.example.android.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.sunshine.data.WeatherContract.WeatherEntry;
import com.example.android.sunshine.util.DateUtil;


public class WeatherProvider extends ContentProvider {

    public static final int CODE_WEATHER = 100;
    public static final int CODE_WEATHER_WITH_DATE = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private WeatherDbHelper mDatabase;


    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WeatherContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, WeatherContract.PATH_WEATHER, CODE_WEATHER);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/#", CODE_WEATHER_WITH_DATE);

        return matcher;
    }


    @Override
    public boolean onCreate() {

        mDatabase = new WeatherDbHelper(getContext());

        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {

        return null;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor data;

        switch (sUriMatcher.match(uri)) {

            case CODE_WEATHER_WITH_DATE: {

                String normalizedUtcDateString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{normalizedUtcDateString};

                data = mDatabase.getReadableDatabase().query(

                        WeatherEntry.TABLE_NAME,
                        projection,
                        WeatherEntry.COLUMN_DATE + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_WEATHER: {

                data = mDatabase.getReadableDatabase().query(
                        WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default: {

                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        data.setNotificationUri(getContext().getContentResolver(), uri);

        return data;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        throw new UnsupportedOperationException("Insert not supported. Use bulkInsert instead.");
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = mDatabase.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_WEATHER: {

                db.beginTransaction();

                int numInserted = 0;

                try {

                    for (ContentValues value : values) {

                        long weatherDate = value.getAsLong(WeatherEntry.COLUMN_DATE);

                        if (!DateUtil.isDateNormalized(weatherDate)) {

                            throw new IllegalArgumentException("Date must be normalized to insert");
                        }

                        long _id = db.insert(WeatherEntry.TABLE_NAME, null, value);

                        if (_id != -1) {

                            numInserted++;
                        }
                    }

                    db.setTransactionSuccessful();

                } finally {

                    db.endTransaction();
                }

                if (numInserted > 0) {

                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return numInserted;
            }

            default: {

                return super.bulkInsert(uri, values);
            }
        }
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        return 0;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int numDeleted;

        if(selection == null) {

            selection = "1";
        }

        switch (sUriMatcher.match(uri)) {

            case CODE_WEATHER: {

                numDeleted = mDatabase.getWritableDatabase().delete(
                        WeatherEntry.TABLE_NAME, selection, selectionArgs);

                break;
            }

            default: {

                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numDeleted > 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numDeleted;
    }
}
