package com.example.android.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.ForecastAdapter.ForecastClickHandler;
import com.example.android.sunshine.data.SunshinePrefs;
import com.example.android.sunshine.data.WeatherContract.WeatherEntry;
import com.example.android.sunshine.util.DateUtil;


public class MainActivity extends AppCompatActivity implements ForecastClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int FORECAST_LOADER_ID = 0x01;

    private static boolean PREFS_UPDATED = false;

    private ForecastAdapter mForecastAdapter;

    private RecyclerView mForecastRecycler;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mForecastRecycler = (RecyclerView) findViewById(R.id.forecast_recycler);
        mForecastRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mForecastRecycler.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(this, this);
        mForecastRecycler.setAdapter(mForecastAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mErrorMessage = (TextView) findViewById(R.id.error_message);

        showLoadingIndicator();

        getSupportLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    protected void onStart() {

        super.onStart();

        if (PREFS_UPDATED) {

            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);

            PREFS_UPDATED = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.forecast, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.action_refresh: {

                invalidateData();

                getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);

                return true;
            }

            case R.id.action_settings: {

                startActivity(new Intent(this, SettingsActivity.class));

                return true;
            }

            case R.id.action_map: {

                launchMap();

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(id != FORECAST_LOADER_ID) {

            throw new RuntimeException("Loader not implemented: " + id);
        }

        String[] projection = {
                WeatherEntry.COLUMN_DATE, WeatherEntry.COLUMN_MAX_TEMP,
                WeatherEntry.COLUMN_MIN_TEMP, WeatherEntry.COLUMN_WEATHER_ID };

        String selection = WeatherEntry.COLUMN_DATE + " >= "
                + DateUtil.normalizeDate(System.currentTimeMillis());

        String sort = WeatherEntry.COLUMN_DATE + " ASC";

        return new CursorLoader(this, WeatherEntry.CONTENT_URI,
                projection, selection, null, sort);

//        return new AsyncTaskLoader<String[]>(this) {
//
//            String[] mWeatherData = null;
//
//
//            @Override
//            protected void onStartLoading() {
//
//                if (mWeatherData != null) {
//
//                    deliverResult(mWeatherData);
//
//                } else {
//
//                    mLoadingIndicator.setVisibility(View.VISIBLE);
//
//                    forceLoad();
//                }
//            }
//
//
//            @Override
//            public String[] loadInBackground() {
//
//                String location = SunshinePrefs.getPreferredLocation(MainActivity.this);
//                URL requestUrl = NetworkUtil.buildUrl(location);
//
//                try {
//
//                    String jsonResponse = NetworkUtil.getHttpResponse(requestUrl);
//
//                    return JsonUtil.parseWeatherStrings(MainActivity.this, jsonResponse);
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//
//            @Override
//            public void deliverResult(String[] result) {
//
//                mWeatherData = result;
//
//                super.deliverResult(result);
//            }
//        };
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mForecastAdapter.swapCursor(data);

        //if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        //mRecyclerView.smoothScrollToPosition(mPosition);

        if (data.getCount() != 0) showWeatherDataView();
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mForecastAdapter.swapCursor(null);
    }


    private void showLoadingIndicator() {

        mForecastRecycler.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String s) {

        PREFS_UPDATED = true;
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onClick(String weatherString) {

        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(Intent.EXTRA_TEXT, weatherString);

        startActivity(detailIntent);
    }


    private void showWeatherDataView() {

        mErrorMessage.setVisibility(View.INVISIBLE);
        mForecastRecycler.setVisibility(View.VISIBLE);
    }


    private void invalidateData() {

        mForecastAdapter.setWeatherData(null);
    }


    private void showErrorMessage() {

        mForecastRecycler.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }


    private void launchMap() {

        String address = SunshinePrefs.getPreferredLocation(this);

        Uri location = Uri.parse("geo:0,0?q=" + address);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(location);

        if (intent.resolveActivity(getPackageManager()) != null) {

            startActivity(intent);

        } else {

            Log.d(LOG_TAG, "Couldn't call " + location.toString()
                    + ", no receiving apps installed!");
        }
    }
}
