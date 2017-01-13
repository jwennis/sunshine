package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private String mForecast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        if(savedInstanceState != null) {

            // TODO: restore instance state

        } else {

            Intent detailIntent = getIntent();

            if (detailIntent.hasExtra(Intent.EXTRA_TEXT)) {

                mForecast = detailIntent.getStringExtra(Intent.EXTRA_TEXT);

            } else {

                // TODO: throw WtfException
            }
        }
    }
}
