package com.example.sushma.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends ActionBarActivity {

    private String mLocation;

    private final String FORECASTFRAGMENT_TAG = "FFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("MainActivity", "onCreate");
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        Log.v("MainActivity", "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.v("MainActivity", "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);

        if(location != null && !location.equals(mLocation)) {
            ForecastFragment ff = (ForecastFragment) getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            if(ff != null) {
                ff.onLocationChanged();
            }
            mLocation = location;
        }
    }

    @Override
    protected void onStop() {
        Log.v("MainActivity", "onStop");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.v("MainActivity", "onStart");
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            startActivity(new Intent().setClass(this, SettingsActivity.class));
            return true;
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }
}
