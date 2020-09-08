package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class QAActivity extends AppCompatActivity  {

    private Toolbar mToolbat;
    private String city;
    private String about,current_user_id,myquestion,saveCurrentDate,saveCurrentTime,randomName;
    private EditText question;
    private Button ask;

    private ProgressDialog loadingBar;

    private StorageReference postImagesRef;
    private DatabaseReference UserRef,QuesRef,PostsRef;
    private FirebaseAuth mAuth;

    private long countPosts=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);

        mToolbat= (Toolbar) findViewById(R.id.b2);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Ask a Question About place/cost etc..");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ////---------------------//////


        Spinner spinner = findViewById(R.id.s2);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.CitiesList,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new class1());

        ////---------------------//////

        Spinner spinner2 = findViewById(R.id.s1);
        ArrayAdapter<CharSequence> adapter2 =  ArrayAdapter.createFromResource(this,R.array.About,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new class2());



        ////---------------------//////

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        QuesRef = FirebaseDatabase.getInstance().getReference().child("Going");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        loadingBar = new ProgressDialog(this);

        question= (EditText) findViewById(R.id.myquestion);
        ask= (Button) findViewById(R.id.ask);



        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidatePostInfo();
            }
        });




}




    private void ValidatePostInfo() {

        myquestion = question.getText().toString();

        if (TextUtils.isEmpty(myquestion)){
            Toast.makeText(this, "Please enter your question first.. ", Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Adding New Post...");
            loadingBar.setMessage("Please wait and be patient.....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            SavingPostsInfoToDB();

        }
    }





    private void SavingPostsInfoToDB() {

        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calDate.getTime());

        Calendar calTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH-mm-ss");
        saveCurrentTime = currentTime.format(calTime.getTime());

        randomName=saveCurrentTime+saveCurrentDate;


        PostsRef.addValueEventListener(new ValueEventListener() {
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


        UserRef.child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){


                    String userFullName = dataSnapshot.child("FullName").getValue().toString();
                    String userProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();
                   String myquestion = question.getText().toString();
                    String sss = "Q-"+about;

                    final HashMap PostsMap = new HashMap();
                    PostsMap.put("uId",current_user_id);
                    PostsMap.put("Date",saveCurrentDate);
                    PostsMap.put("Time",saveCurrentTime);
                    PostsMap.put("UserProfileImage",userProfileImage);
                    PostsMap.put("FullName",userFullName);
                    PostsMap.put("Category",sss);
                    PostsMap.put("City",city);
                    PostsMap.put("Description",myquestion);
                    PostsMap.put("Counter",countPosts);
                    PostsMap.put("PostImage","");



                                PostsRef.child(current_user_id+randomName).updateChildren(PostsMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {

                                        SendUserToMainActivity();
                                        Toast.makeText(QAActivity.this, "Post Published", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(QAActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


/////----------------------------------////////

    private class class1 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if (adapterView.getItemAtPosition(i).equals("City")){

                Toast.makeText(QAActivity.this, "Please Chose a City ", Toast.LENGTH_SHORT).show();            }
            else{

                city = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(QAActivity.this, city, Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }








    private class class2 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getItemAtPosition(i).equals("About")){

                Toast.makeText(QAActivity.this, "Please Chose a type ", Toast.LENGTH_SHORT).show();            }
            else{

                about = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(QAActivity.this, about, Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
