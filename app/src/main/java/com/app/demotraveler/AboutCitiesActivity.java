package com.app.demotraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutCitiesActivity extends AppCompatActivity {


    private Toolbar mToolbat;
    private TextView ist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_cities);


        mToolbat= (Toolbar) findViewById(R.id.cities_page_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Cities");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    ist=(TextView) findViewById(R.id.ist_city);

    ist.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SendUserToIstanbulActivity();
        }
    });




    }

    private void SendUserToIstanbulActivity() {
        Intent mainIntent = new Intent(AboutCitiesActivity.this, IstanbulActivity.class);
        startActivity(mainIntent);
    }
}
