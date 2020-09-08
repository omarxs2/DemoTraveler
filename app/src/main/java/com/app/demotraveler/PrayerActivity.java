package com.app.demotraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PrayerActivity extends AppCompatActivity {

    private Toolbar mToolbat;
    private TextView Fajr,Duhur,Asr,Magrib,Isha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);

        mToolbat= (Toolbar) findViewById(R.id.prayer_page_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Prayer Time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Fajr=  (TextView) findViewById(R.id.fajr);
        Duhur=  (TextView) findViewById(R.id.duhr);
        Asr=  (TextView) findViewById(R.id.asr);
        Magrib=  (TextView) findViewById(R.id.magrib);
        Isha=  (TextView) findViewById(R.id.isha);


        final Coordinates coordinates = new Coordinates(41.0082, 28.9784);
        final DateComponents dateComponents = DateComponents.from(new Date());
        final CalculationParameters parameters =
                CalculationMethod.UMM_AL_QURA.getParameters();

        SimpleDateFormat formatter = new SimpleDateFormat("kk:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Istanbul"));


        PrayerTimes prayerTimes = new PrayerTimes(coordinates, dateComponents, parameters);

        formatter.format(prayerTimes.fajr);


        Fajr.setText(formatter.format(prayerTimes.fajr));
        Duhur.setText(formatter.format(prayerTimes.dhuhr));
        Asr.setText(formatter.format(prayerTimes.asr));
        Magrib.setText(formatter.format(prayerTimes.maghrib));
        Isha.setText(formatter.format(prayerTimes.isha));



    }
}
