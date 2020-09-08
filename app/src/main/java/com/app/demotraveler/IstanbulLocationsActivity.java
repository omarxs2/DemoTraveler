package com.app.demotraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class IstanbulLocationsActivity extends AppCompatActivity {
    private Toolbar mToolbat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istanbul_locations);





            mToolbat= (Toolbar) findViewById(R.id.ist_loc_toolbar);
            setSupportActionBar(mToolbat);
            getSupportActionBar().setTitle("Istanbul Tourist Locations");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
