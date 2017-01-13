package com.example.android.sunshine;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.ForecastAdapter.ForecastClickHandler;
import com.example.android.sunshine.data.SunshinePrefs;
import com.example.android.sunshine.util.JsonUtil;
import com.example.android.sunshine.util.NetworkUtil;

import java.net.URL;


public class MainActivity extends AppCompatActivity implements ForecastClickHandler {

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

        mForecastAdapter = new ForecastAdapter(this);
        mForecastRecycler.setAdapter(mForecastAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mErrorMessage = (TextView) findViewById(R.id.error_message);

        loadWeather();
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

                mForecastAdapter.setWeatherData(null);

                loadWeather();

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }


    @Override
    public void onClick(String weatherString) {

        Intent detailIntent = new Intent(this, DetailActivity.class);

        startActivity(detailIntent);
    }


    private void loadWeather() {

        showWeatherDataView();

        String location = SunshinePrefs.getPreferredLocation(this);

        new FetchWeatherTask().execute(location);
    }


    private void showWeatherDataView() {

        mErrorMessage.setVisibility(View.INVISIBLE);
        mForecastRecycler.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {

        mForecastRecycler.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }


    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {

                return null;
            }

            String location = params[0];

            URL requestUrl = NetworkUtil.buildUrl(location);

            try {

                String jsonResponse = NetworkUtil.getHttpResponse(requestUrl);

                return JsonUtil.parseWeatherStrings(MainActivity.this, jsonResponse);

            } catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String[] result) {

            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (result != null) {

                showWeatherDataView();

                mForecastAdapter.setWeatherData(result);

            } else {

                showErrorMessage();
            }
        }
    }
}
