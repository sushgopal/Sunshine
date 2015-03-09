package com.example.sushma.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sushma.sunshine.app.data.WeatherContract;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent().setClass(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private static final String[] FORECAST_COLUMNS = { WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
        WeatherContract.WeatherEntry.COLUMN_DATE,
        WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP };

        static final int COL_WEATHER_ID = 0;
        static final int COL_WEATHER_DATE = 1;
        static final int COL_WEATHER_DESC = 2;
        static final int COL_WEATHER_MAX_TEMP = 3;
        static final int COL_WEATHER_MIN_TEMP = 4;

        private ShareActionProvider shareActionProvider;

        private String forecastDetail;

        private static final int DETAIL_LOADER = 0;

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
            return inflater.inflate(R.layout.fragment_detail, container, false);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Intent intent = getActivity().getIntent();
            if(intent == null) {
                return null;
            }

            return new CursorLoader(getActivity(), intent.getData(), FORECAST_COLUMNS, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if(!data.moveToFirst()) { return; }

            String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
            String weatherDescription = data.getString(COL_WEATHER_DESC);
            boolean isMetric = Utility.isMetric(getActivity());

            String high = Utility.formatTemperature(data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);

            String low = Utility.formatTemperature(data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

            forecastDetail = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

            TextView detailTextView = (TextView) getView().findViewById(R.id.detail_text);
            detailTextView.setText(forecastDetail);

            if(shareActionProvider != null) {
                shareActionProvider.setShareIntent(createShareIntent());
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
