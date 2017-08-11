package com.justApp.RadioPlayer.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.justApp.RadioPlayer.R;
import com.justApp.RadioPlayer.ui.fragment.StationsFragment;
import com.justApp.RadioPlayer.utils.ActivityUtils;

public class StationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StationsFragment stationsFragment = (StationsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);
        if (stationsFragment == null) {
            stationsFragment = StationsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    stationsFragment, R.id.container);
        }
    }
}
