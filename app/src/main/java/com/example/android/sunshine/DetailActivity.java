package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private String mForecast;
    private TextView mWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        mWeatherData = (TextView) findViewById(R.id.weather_data);

        if(savedInstanceState != null) {

            // TODO: restore instance state

        } else {

            Intent detailIntent = getIntent();

            if (detailIntent.hasExtra(Intent.EXTRA_TEXT)) {

                mForecast = detailIntent.getStringExtra(Intent.EXTRA_TEXT);

                mWeatherData.setText(mForecast);

            } else {

                // TODO: throw WtfException
            }
        }
    }
}
