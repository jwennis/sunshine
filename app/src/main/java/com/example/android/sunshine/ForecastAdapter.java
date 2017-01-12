package com.example.android.sunshine;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>  {

    private String[] mWeatherData;


    @Override
    public ForecastAdapter.ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }


    @Override
    public void onBindViewHolder(ForecastAdapter.ForecastViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {

        return mWeatherData != null ? mWeatherData.length : 0;
    }


    public void setWeatherData(String[] data) {

        mWeatherData = data;

        notifyDataSetChanged();
    }


    public class ForecastViewHolder extends RecyclerView.ViewHolder {


        public ForecastViewHolder(View itemView) {

            super(itemView);
        }
    }
}
