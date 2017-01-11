package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePrefs;
import com.example.android.sunshine.util.NetworkUtil;

import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private TextView mWeatherText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mWeatherText = (TextView) findViewById(R.id.weather_data);

        String location = SunshinePrefs.getPreferredLocation(this);

        new FetchWeatherTask().execute(location);
    }


    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {


        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {

                return null;
            }

            String location = params[0];

            URL requestUrl = NetworkUtil.buildUrl(location);

            return null;
        }


        @Override
        protected void onPostExecute(String[] result) {

        }
    }
}
