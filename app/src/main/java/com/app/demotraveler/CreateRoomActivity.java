package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CreateRoomActivity extends AppCompatActivity {

    private CheckBox c1food,c2history,c3fest,c4mus,c5sport,
            c6activ,c7party,c8nature,c9shopping,c10relig,
            c11recreation,c12culture,c13art,c14sea;


    private Button CreateRoomBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference RoomsRef,UserRef;
    private String current_user_id,City,StartingDate,EndingDate,Looking,UserProfileImage;
    private Spinner spinner,spinner2;
    private EditText desc;
    private TextView DateStart,DateEnd;

    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetListener2;

    private static final String TAG = "CreateRoomActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);


        Toolbar mToolbat= (Toolbar) findViewById(R.id.book_local_bar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Create Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        spinner = findViewById(R.id.spinner_city112);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.CitiesList,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new class1());


        ///////------------------------//////////


        spinner2 = findViewById(R.id.spinner_room_needs);
        ArrayAdapter<CharSequence> adapter2 =  ArrayAdapter.createFromResource(this,R.array.Looking,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new class2());



        c1food = (CheckBox)findViewById(R.id.ckfood);
        c2history = (CheckBox)findViewById(R.id.ckhistory);
        c3fest = (CheckBox)findViewById(R.id.ckfestivals);
        c4mus = (CheckBox)findViewById(R.id.ckmuseum);
        c5sport = (CheckBox)findViewById(R.id.cksport);
        c6activ = (CheckBox)findViewById(R.id.ckactivities);
        c7party = (CheckBox)findViewById(R.id.ckparty);
        c8nature = (CheckBox)findViewById(R.id.cknature);
        c9shopping = (CheckBox)findViewById(R.id.ckshopping);
        c10relig = (CheckBox)findViewById(R.id.ckreligious);
        c11recreation = (CheckBox)findViewById(R.id.ckrecreation);
        c12culture = (CheckBox)findViewById(R.id.ckculture);
        c13art = (CheckBox)findViewById(R.id.ckart);
        c14sea = (CheckBox)findViewById(R.id.cksea);

        desc= (EditText) findViewById(R.id.room_desc);
        DateStart = (TextView) findViewById(R.id.date_start_room);
        DateEnd = (TextView) findViewById(R.id.date_end_room);

        CreateRoomBtn = (Button) findViewById(R.id.createBtn);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        RoomsRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        CreateRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDateToDatabase();

            }
        });



        DateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateRoomActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                StartingDate = day + "/" + month + "/" + year;
                DateStart.setText(StartingDate);
            }
        };



        DateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateRoomActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                EndingDate = day + "/" + month + "/" + year;
                DateEnd.setText(EndingDate);
            }
        };



    }

    private void SaveDateToDatabase() {


        final HashMap userMap2 = new HashMap();



        if (c1food.isChecked() == true){
            userMap2.put("Food" ,"true");
        }
        if (c2history.isChecked() == true){
            userMap2.put("History" ,"true");
        }
        if (c3fest.isChecked() == true){
            userMap2.put("Festivals" ,"true");
        }
        if (c4mus.isChecked() == true){
            userMap2.put("Museums" ,"true");
        }
        if (c5sport.isChecked() == true){
            userMap2.put("Sport" ,"true");
        }
        if (c6activ.isChecked() == true){
            userMap2.put("Activities","true");
        }
        if (c7party.isChecked() == true){
            userMap2.put("Party","true");
        }
        if (c8nature.isChecked() == true){
            userMap2.put("Nature","true");
        }
        if (c9shopping.isChecked() == true){
            userMap2.put("Shopping" ,"true");
        }
        if (c10relig.isChecked() == true){
            userMap2.put("Religious","true");
        }
        if (c11recreation.isChecked() == true){
            userMap2.put("Recreation" ,"true");
        }
        if (c12culture.isChecked() == true){
            userMap2.put("Culture" ,"true");
        }
        if (c13art.isChecked() == true){
            userMap2.put("Art","true");
        }
        if (c14sea.isChecked() == true){
            userMap2.put("Sea","true");
        }

        ////-----////


        if (c1food.isChecked() == false){
            userMap2.put("Food" ,"false");
        }
        if (c2history.isChecked() == false){
            userMap2.put("History" ,"false");
        }
        if (c3fest.isChecked() == false){
            userMap2.put("Festivals" ,"false");
        }
        if (c4mus.isChecked() == false){
            userMap2.put("Museums" ,"false");
        }
        if (c5sport.isChecked() == false){
            userMap2.put("Sport" ,"false");
        }
        if (c6activ.isChecked() == false){
            userMap2.put("Activities","false");
        }
        if (c7party.isChecked() == false){
            userMap2.put("Party","false");
        }
        if (c8nature.isChecked() == false){
            userMap2.put("Nature","false");
        }
        if (c9shopping.isChecked() == false){
            userMap2.put("Shopping" ,"false");
        }
        if (c10relig.isChecked() == false){
            userMap2.put("Religious","false");
        }
        if (c11recreation.isChecked() == false){
            userMap2.put("Recreation" ,"false");
        }
        if (c12culture.isChecked() == false){
            userMap2.put("Culture" ,"false");
        }
        if (c13art.isChecked() == false){
            userMap2.put("Art","false");
        }
        if (c14sea.isChecked() == false){
            userMap2.put("Sea","false");
        }


/*
        UserRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                   UserProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/



        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate = currentDate.format(calDate.getTime());

        Calendar calTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH-mm-ss");
        String saveCurrentTime = currentTime.format(calTime.getTime());

        final String randomName=saveCurrentTime+saveCurrentDate;



        userMap2.put("Description",desc.getText().toString());
        userMap2.put("City",City);
        userMap2.put("Looking",Looking);
        userMap2.put("Date1",StartingDate);
        userMap2.put("Date2",EndingDate);
        userMap2.put("UId",current_user_id);
        userMap2.put("CurrentDate",saveCurrentDate);
      //  userMap2.put("UserProfileImage",UserProfileImage);




            RoomsRef.child(current_user_id+randomName+"Room")
                    .setValue(userMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                                   // HashMap userMap3 = new HashMap();

                                  //  userMap3.put("Date","true");
                                    UserRef.child(current_user_id).child("My Rooms")
                                    .child(current_user_id+randomName+"Room").setValue(userMap2)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            SendUserToAllRoomsActivity();
                                     }
                         });


                    }

                }
            });


        }





    private void SendUserToAllRoomsActivity() {

        Intent mainIntent = new Intent(CreateRoomActivity.this, JoindRoomsActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }


    private class class1 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if (adapterView.getItemAtPosition(i).equals("City")){

                Toast.makeText(CreateRoomActivity.this, "Please Chose a City ", Toast.LENGTH_SHORT).show();            }
            else{

                City = adapterView.getItemAtPosition(i).toString();

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }








    private class class2 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getItemAtPosition(i).equals("Looking for")){

                Toast.makeText(CreateRoomActivity.this, "Please Select first ", Toast.LENGTH_SHORT).show();            }
            else{

                Looking = adapterView.getItemAtPosition(i).toString();

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }







}
