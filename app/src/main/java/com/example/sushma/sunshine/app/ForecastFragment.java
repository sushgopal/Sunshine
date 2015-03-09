package com.example.sushma.sunshine.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ForecastFragment extends Fragment {

        ArrayAdapter<String> adapter;

        public ForecastFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            adapter = new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.list_item_forecast,
                    R.id.list_item_forecast_textview,
                    new ArrayList<String>());

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String forecastData = adapter.getItem(position);

                    Intent detailActivityIntent = new Intent();
                    detailActivityIntent.setClass(getActivity(), DetailActivity.class);
                    detailActivityIntent.putExtra(Intent.EXTRA_TEXT, forecastData);
                    startActivity(detailActivityIntent);
                }
            });
            return rootView;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.forecast_fragment, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int selectedId = item.getItemId();
            if(selectedId == R.id.action_refresh) {
                updateWeather();
                return true;
            }
            if(selectedId == R.id.action_preferred_location) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri location = Uri.parse("geo:0,0?q="+getPreferredLocation());
                intent.setData(location);
                if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onStart() {
            super.onStart();
            updateWeather();
        }

    private void updateWeather() {
        String location = getPreferredLocation();
        new FetchWeatherTask(getActivity(), adapter).execute(new String[] {location});
    }

    private String getPreferredLocation() {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());

        return sharedPreferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default_value));
    }
}
