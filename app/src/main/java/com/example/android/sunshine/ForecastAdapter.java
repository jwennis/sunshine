package com.example.android.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.util.DateUtil;
import com.example.android.sunshine.util.WeatherUtil;

import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_DATE;
import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_MAX_TEMP;
import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_MIN_TEMP;
import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_WEATHER_ID;

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

        mCursor.moveToPosition(position);

        long dateMs = mCursor.getLong(getColumn(COLUMN_DATE));
        String datestring = DateUtil.getFriendlyDateString(mContext, dateMs, false);

        int weatherId = mCursor.getInt(getColumn(COLUMN_WEATHER_ID));
        String description = WeatherUtil.getConditionFromId(mContext, weatherId);

        double high = mCursor.getDouble(getColumn(COLUMN_MAX_TEMP));
        double low = mCursor.getDouble(getColumn(COLUMN_MIN_TEMP));
        String tempstring = WeatherUtil.formatHighLows(mContext, high, low);

        holder.mWeatherText.setText(String.format("%s - %s - %s",
                datestring + " - " + description + " - " + tempstring));
    }


    private int getColumn(String column) {

        return mCursor.getColumnIndex(column);
    }


    @Override
    public int getItemCount() {

        return mCursor != null ? mCursor.getCount() : 0;
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

            mClickHandler.onClick(mWeatherText.getText().toString());
        }
    }
}
