package com.app.demotraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class IstanbulSch2Activity extends AppCompatActivity {
    private Toolbar mToolbat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istanbul_sch2);

        mToolbat= (Toolbar) findViewById(R.id.ist_sce2_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("5 Days Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




    }
}
