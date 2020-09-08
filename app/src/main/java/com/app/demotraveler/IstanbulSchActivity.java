package com.app.demotraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IstanbulSchActivity extends AppCompatActivity {

    private Toolbar mToolbat;
    private Button FiveDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istanbul_sch);



        mToolbat= (Toolbar) findViewById(R.id.ist_daily_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Daily Schedules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        FiveDays = (Button) findViewById(R.id.sch2);
        FiveDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserTo5DaysSchedulesActivity();
            }
        });

    }

    private void SendUserTo5DaysSchedulesActivity() {
        Intent mainIntent = new Intent(IstanbulSchActivity.this, IstanbulSch2Activity.class);
        startActivity(mainIntent);

    }
}
