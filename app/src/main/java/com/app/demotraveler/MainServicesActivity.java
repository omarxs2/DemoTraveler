package com.app.demotraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainServicesActivity extends AppCompatActivity {



    private Toolbar mToolbat;
    private Button LocalGuid,Group,PlanTrip,BookNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_services);





        mToolbat= (Toolbar) findViewById(R.id.services_bar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Main Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        LocalGuid = findViewById(R.id.local_guid_btn);
        Group = findViewById(R.id.make_group_btn);
        PlanTrip = findViewById(R.id.plan_trip_btn);
        BookNow = findViewById(R.id.book_btn);


        LocalGuid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                SendUserToLocalGuidActivity();

            }
        });


        Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToMakeGroupActivity();


            }
        });

        PlanTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToPlanTripActivity();


            }
        });

        BookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToBookNowActivity();


            }
        });

        PlanTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToAboutCitiesActivity();


            }
        });



    }


    private void SendUserToAboutCitiesActivity() {
        Intent setupIntent2 = new Intent(MainServicesActivity.this, AboutCitiesActivity.class);
        startActivity(setupIntent2);
    }


    private void SendUserToBookNowActivity() {



    }

    private void SendUserToPlanTripActivity() {

    }

    private void SendUserToMakeGroupActivity() {
        Intent mainIntent = new Intent(MainServicesActivity.this, RoomActivity.class);
        startActivity(mainIntent);
    }

    private void SendUserToLocalGuidActivity() {
        Intent mainIntent = new Intent(MainServicesActivity.this, LocalGuidActivity.class);
        startActivity(mainIntent);
    }


}
