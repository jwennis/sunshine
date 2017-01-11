package com.example.android.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePrefs;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mWeatherText = (TextView) findViewById(R.id.weather_data);

        String location = SunshinePrefs.getPreferredLocation(this);
    }
}
