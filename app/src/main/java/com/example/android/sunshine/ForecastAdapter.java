package com.example.android.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private final ForecastClickHandler mClickHandler;
    private final Context mContext;

    private Cursor mCursor;
    private String[] mWeatherData;


    public ForecastAdapter() {

        mContext = null;
        mClickHandler = null;
    }


    public ForecastAdapter(ForecastClickHandler handler) {

        mContext = null;
        mClickHandler = handler;
    }


    public ForecastAdapter(Context context, ForecastClickHandler handler) {

        mContext = context;
        mClickHandler = handler;
    }


    @Override
    public ForecastAdapter.ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_forecast, parent, false);

        itemView.setFocusable(true);

        return new ForecastViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ForecastAdapter.ForecastViewHolder holder, int position) {

        String item = getItemAt(position);

        holder.mWeatherText.setText(item);
    }


    @Override
    public int getItemCount() {

        return mWeatherData != null ? mWeatherData.length : 0;
    }


    public String getItemAt(int pos) {

        return pos < getItemCount() ? mWeatherData[ pos ] : null;
    }


    public void setWeatherData(String[] data) {

        mWeatherData = data;

        notifyDataSetChanged();
    }


    public void swapCursor(Cursor newCursor) {

        mCursor = newCursor;

        notifyDataSetChanged();
    }


    public interface ForecastClickHandler {

        void onClick(String weatherString);
    }


    public class ForecastViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final TextView mWeatherText;


        public ForecastViewHolder(View itemView) {

            super(itemView);

            mWeatherText = (TextView) itemView.findViewById(R.id.weather_data);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            int pos = getAdapterPosition();

            mClickHandler.onClick(getItemAt(pos));
        }
    }
}
