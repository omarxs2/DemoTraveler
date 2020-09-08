package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class GoingActivity extends AppCompatActivity {

    private static final String TAG = "GoingActivity";

    private Toolbar mToolbat;
    private String city1,city2,curentUserID,date1,date2,UserNotes;
    private DatabaseReference UserRef,GoingRef;
    private FirebaseAuth mAuth;
    private long countPosts=0;
    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetListener2;
    private TextView Date1,Date2;
    private EditText Notes;
    private Button Post;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going);

        mToolbat= (Toolbar) findViewById(R.id.going_bar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Who is going");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ////---------------------//////

        Spinner spinner = findViewById(R.id.spinner_from);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.CitiesList,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new class1());


        ////---------------------//////

        Spinner spinner2 = findViewById(R.id.spinner_to);
        ArrayAdapter<CharSequence> adapter2 =  ArrayAdapter.createFromResource(this,R.array.CitiesList,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new class2());

        ////---------------------//////


        Date1 = (TextView) findViewById(R.id.date1);
        Date2 = (TextView) findViewById(R.id.date2);
        Notes = (EditText) findViewById(R.id.snotes);
        Post = (Button) findViewById(R.id.going_post_btn);


        ////---------------------//////

        mAuth=FirebaseAuth.getInstance();
        curentUserID=mAuth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        GoingRef= FirebaseDatabase.getInstance().getReference().child("WhoGoing");



        ////---------------------//////


        Date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        GoingActivity.this,
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

                date1 = day + "/" + month + "/" + year;
                Date1.setText(date1);
            }
        };



        Date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        GoingActivity.this,
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

                date2 = day + "/" + month + "/" + year;
                Date2.setText(date2);
            }
        };


        ////---------------------//////

        GoingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    countPosts = dataSnapshot.getChildrenCount();
                }
                else{
                    countPosts=0;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ///////////////////////////

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                String saveCurrentDate = currentDate.format(calDate.getTime());

                Calendar calTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH-mm-ss");
                String saveCurrentTime = currentTime.format(calTime.getTime());



                final String RequestID=curentUserID+saveCurrentTime+saveCurrentDate+"GoingRequest";



                UserNotes = Notes.getText().toString();

                final HashMap PostsMap = new HashMap();
                PostsMap.put("From", city1);
                PostsMap.put("To", city2);
                PostsMap.put("Date1",date1);
                PostsMap.put("Date2",date2);
                PostsMap.put("Notes",UserNotes);
                PostsMap.put("UserID", curentUserID);
                PostsMap.put("Date", saveCurrentDate);
                PostsMap.put("Time", saveCurrentTime);
                PostsMap.put("Counter", countPosts);


                GoingRef.child(RequestID).updateChildren(PostsMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Toast.makeText(GoingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            SendUserToMainActivity();
                        }
                    }
                });

            }
        });



        }













    ///----------------------------------------///


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(GoingActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }



    ///----------------------------------------///



    private class class1 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getItemAtPosition(i).equals("City")){

                Toast.makeText(GoingActivity.this, "Please Chose a City ", Toast.LENGTH_SHORT).show();            }
            else{

                city1 = adapterView.getItemAtPosition(i).toString();

            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    ///----------------------------------------///

    private class class2 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getItemAtPosition(i).equals("City")){

                Toast.makeText(GoingActivity.this, "Please Chose a City ", Toast.LENGTH_SHORT).show();            }
            else{

                city2 = adapterView.getItemAtPosition(i).toString();

            }
        }


            @Override
            public void onNothingSelected (AdapterView < ? > adapterView){

            }
        }



}
