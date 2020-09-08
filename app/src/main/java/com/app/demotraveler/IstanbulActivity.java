package com.app.demotraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IstanbulActivity extends AppCompatActivity {
    private TextView info,touristLoc,weather,pray,hotels,daily;
    private Toolbar mToolbat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istanbul);


        mToolbat= (Toolbar) findViewById(R.id.ist_page_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Istanbul");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        info = (TextView) findViewById(R.id.info_ist);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToAboutIstanbulActivity();
            }
        });


        touristLoc = (TextView) findViewById(R.id.tourist_loc_ist);
        touristLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToIstanbulLocationsActivity();
            }
        });



        weather = (TextView) findViewById(R.id.weather_ist);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToIstanbulWeatherActivity();
            }
        });


        pray = (TextView) findViewById(R.id.prayer_ist);
        pray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToIstanbulPrayActivity();
            }
        });


        hotels = (TextView) findViewById(R.id.hotels_ist);
        hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToIstanbulHotelsActivity();
            }
        });

        daily = (TextView) findViewById(R.id.ist_daily);
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToIstanbulSchActivity();
            }
        });






    }

    private void SendUserToIstanbulSchActivity() {
        Intent mainIntent = new Intent(IstanbulActivity.this, IstanbulSchActivity.class);
        startActivity(mainIntent);
    }

    private void SendUserToIstanbulHotelsActivity() {
        Intent mainIntent = new Intent(IstanbulActivity.this, IstanbulHotelsActivity.class);
        startActivity(mainIntent);
    }

    private void SendUserToIstanbulPrayActivity() {
        Intent mainIntent = new Intent(IstanbulActivity.this, PrayerActivity.class);
        startActivity(mainIntent);
    }

    private void SendUserToIstanbulWeatherActivity() {
        Intent mainIntent = new Intent(IstanbulActivity.this, IstanbulWeatherActivity.class);
        startActivity(mainIntent);
    }

    private void SendUserToIstanbulLocationsActivity() {
        Intent mainIntent = new Intent(IstanbulActivity.this, IstanbulLocationsActivity.class);
        startActivity(mainIntent);
    }


    private void SendUserToAboutIstanbulActivity() {
        Intent mainIntent = new Intent(IstanbulActivity.this, InfoIstanbulActivity.class);
        startActivity(mainIntent);
    }



}
