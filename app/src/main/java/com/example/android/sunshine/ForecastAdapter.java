package com.example.android.sunshine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>  {

    private String[] mWeatherData;


    public ForecastAdapter() {

    }


    @Override
    public ForecastAdapter.ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ForecastViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_forecast, parent, false));
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

        public final TextView mWeatherText;


        public ForecastViewHolder(View itemView) {

            super(itemView);

            mWeatherText = (TextView) itemView.findViewById(R.id.weather_data);
        }
    }
}
