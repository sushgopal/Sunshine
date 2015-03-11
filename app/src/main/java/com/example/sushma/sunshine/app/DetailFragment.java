package com.example.sushma.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sushma.sunshine.app.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] DETAIL_COLUMNS = { WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
    WeatherContract.WeatherEntry.COLUMN_DATE,
    WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
    WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
    WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
    WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
    WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
    WeatherContract.WeatherEntry.COLUMN_DEGREES,
    WeatherContract.WeatherEntry.COLUMN_PRESSURE,
    WeatherContract.WeatherEntry.COLUMN_WEATHER_ID};

    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_WEATHER_HUMIDITY = 5;
    static final int COL_WEATHER_WIND_SPEED = 6;
    static final int COL_WEATHER_DEGREES = 7;
    static final int COL_WEATHER_PRESSURE = 8;
    static final int COL_WEATHER_CONDITION_ID = 9;

    private ShareActionProvider shareActionProvider;

    private String forecastDetail;



    private static final int DETAIL_LOADER = 0;
    private TextView mDayView;
    private TextView mDateView;
    private TextView mHighView;
    private TextView mLowView;
    private TextView mHumidityView;
    private TextView mWindSpeedView;
    private TextView mPressureView;
    private TextView mDescView;
    private ImageView mWeatherConditionView;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        setShareActionIntent(menu);
    }

    private void setShareActionIntent(Menu menu) {
        Intent shareIntent = createShareIntent();

        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
        if(forecastDetail != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, forecastDetail+" #SunshineApp");
        shareIntent.setType("text/plain");
        return shareIntent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mDayView = (TextView) view.findViewById(R.id.details_day_textview);
        mDateView = (TextView) view.findViewById(R.id.details_date_textview);
        mHighView = (TextView) view.findViewById(R.id.details_high_textview);
        mLowView = (TextView) view.findViewById(R.id.details_low_textview);
        mHumidityView = (TextView) view.findViewById(R.id.details_humidity_textview);
        mWindSpeedView = (TextView) view.findViewById(R.id.details_wind_textview);
        mPressureView = (TextView) view.findViewById(R.id.details_pressure_textview);
        mDescView = (TextView) view.findViewById(R.id.details_description_textview);
        mWeatherConditionView = (ImageView) view.findViewById(R.id.details_weather_image);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if(intent == null) {
            return null;
        }

        return new CursorLoader(getActivity(), intent.getData(), DETAIL_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst()) { return; }

        int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);
        mWeatherConditionView.setImageResource(R.drawable.ic_launcher);

        String day = Utility.getDayName(getActivity(), data.getLong(COL_WEATHER_DATE));
        mDayView.setText(day);

        String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
        mDateView.setText(dateString);

        String weatherDescription = data.getString(COL_WEATHER_DESC);
        mDescView.setText(weatherDescription);

        boolean isMetric = Utility.isMetric(getActivity());
        String high = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        mHighView.setText(high);

        String low = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);
        mLowView.setText(low);

        double humidity = data.getDouble(COL_WEATHER_HUMIDITY);
        mHumidityView.setText(String.format(getActivity().getString(R.string.format_humidity), humidity));

        String windSpeed = Utility.getFormattedWind(getActivity(), data.getFloat(COL_WEATHER_WIND_SPEED), data.getFloat(COL_WEATHER_DEGREES));
        mWindSpeedView.setText(windSpeed);

        double pressure  = data.getDouble(COL_WEATHER_PRESSURE);
        mPressureView.setText(String.format(getActivity().getString(R.string.format_pressure), pressure));

        if(shareActionProvider != null) {
            shareActionProvider.setShareIntent(createShareIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
