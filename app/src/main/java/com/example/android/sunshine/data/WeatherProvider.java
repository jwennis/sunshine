package com.example.android.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

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
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        return null;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        return null;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        return 0;
    }


    @Override
    public int delete(Uri uri, String s, String[] strings) {

        return 0;
    }
}
