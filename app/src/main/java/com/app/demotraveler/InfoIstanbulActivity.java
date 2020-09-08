package com.app.demotraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class InfoIstanbulActivity extends AppCompatActivity {


    private Toolbar mToolbat;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_istanbul);

        mToolbat= (Toolbar) findViewById(R.id.ist_info_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("General Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
