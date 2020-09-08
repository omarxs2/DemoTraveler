package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class SchPostsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar mToolbat;
    private Button opendialog,savebtn,donebtn;
    final Context context = this;
    private DatabaseReference UserRef,SchedulesRef,DaysRef;
    private FirebaseAuth mAuth;
    private String curentUserID;
    private EditText title,cost,tags;
    private String SchKey,loc;
    private long countPosts=0;
    private long countAct=0;
    private Spinner spinner;

    private RecyclerView daysList;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sch_posts);



        daysList = (RecyclerView)findViewById(R.id.user_day_list);
        daysList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        daysList.setLayoutManager(linearLayoutManager);




        title=(EditText)findViewById(R.id.sch_title);
       // loc=(EditText)findViewById(R.id.sch_title);
        cost=(EditText)findViewById(R.id.sch_cost);
        tags=(EditText)findViewById(R.id.sch_tags);


        spinner = findViewById(R.id.sch_location);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.CitiesList,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);




        mAuth=FirebaseAuth.getInstance();
        curentUserID=mAuth.getCurrentUser().getUid();

        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        SchedulesRef= FirebaseDatabase.getInstance().getReference().child("Schedules");


        mToolbat= (Toolbar) findViewById(R.id.b1);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Plan a trip");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        opendialog=(Button)findViewById(R.id.addday);
        savebtn=(Button)findViewById(R.id.save);
        donebtn=(Button)findViewById(R.id.submittrip);


        savebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar calDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                final String saveCurrentDate = currentDate.format(calDate.getTime());

                Calendar calTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH-mm-ss");
                final String  saveCurrentTime = currentTime.format(calTime.getTime());


                SchedulesRef.addValueEventListener(new ValueEventListener() {
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




                UserRef.child(curentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            String userFullName = dataSnapshot.child("FullName").getValue().toString();
                            String userProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();


                            SchKey = curentUserID+saveCurrentDate+saveCurrentTime;

                            final String titlee = title.getText().toString();
                          //  final String location = loc.getText().toString();
                            String tag = tags.getText().toString();
                            String costt = cost.getText().toString();


                            final HashMap hashMap1 = new HashMap();

                            hashMap1.put("Title", titlee);
                            hashMap1.put("Location", loc);
                            hashMap1.put("Cost", costt);
                            hashMap1.put("Tags", tag);
                            hashMap1.put("FullName", userFullName);
                            hashMap1.put("ProfileImage", userProfileImage);
                            hashMap1.put("uId", curentUserID);
                            hashMap1.put("Time", saveCurrentTime);
                            hashMap1.put("Date", saveCurrentDate);
                            hashMap1.put("Counter", countPosts);

                            SchedulesRef.child(SchKey).updateChildren(hashMap1).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    HashMap hashMap2 = new HashMap();

                                    hashMap2.put("Date", saveCurrentDate);

                                    UserRef.child(curentUserID).child("Schedules").child(SchKey)
                                            .updateChildren(hashMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            savebtn.setVisibility(View.INVISIBLE);
                                            opendialog.setVisibility(View.VISIBLE);
                                            donebtn.setVisibility(View.VISIBLE);

                                        }
                                    });




                                }
                            });



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });




        donebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                androidx.appcompat.app.AlertDialog.Builder builder =
                        new androidx.appcompat.app.AlertDialog.Builder(SchPostsActivity.this);
                builder.setTitle("Submit");

                final TextView textView = new TextView(SchPostsActivity.this);
                textView.setText("Are you sure you want finish this schedule?");
                builder.setView(textView);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SendUserToMainActivity();
                    }
                });


                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                Dialog dialog =  builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);


            }
        });



                // add button listener
                opendialog.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        SchedulesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(SchKey).exists()){
                                    DisplayAllDays();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        TextView textView = new TextView(context);
                        textView.setText("Start Planning:");
                        textView.setPadding(20, 30, 20, 30);
                        textView.setTextSize(20F);
                        textView.setBackgroundColor(Color.BLACK);
                        textView.setTextColor(Color.WHITE);

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SchPostsActivity.this);
                        View view= getLayoutInflater().inflate(R.layout.all_days_dialog,null);
                        mBuilder.setCustomTitle(textView);



                        final EditText dayno,type,place,description,loc;

                        dayno=(EditText)view.findViewById(R.id.dialog_day_no);
                        type=(EditText)view.findViewById(R.id.dialog_type);
                        place=(EditText)view.findViewById(R.id.dialog_place);
                        description=(EditText)view.findViewById(R.id.dialog_desc);
                        loc=(EditText)view.findViewById(R.id.dialog_loc);

                        SchedulesRef.child(SchKey).child("Activities").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){

                                    countAct = -1*(dataSnapshot.getChildrenCount()+1);
                                }
                                else{
                                    countAct=1;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        mBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {

                                String no = dayno.getText().toString();
                                String types = type.getText().toString();
                                String places = place.getText().toString();
                                String desc = description.getText().toString();
                                String locs = loc.getText().toString();

                                HashMap hashMap = new HashMap();

                                hashMap.put("Activity",no);
                                hashMap.put("Type",types);
                                hashMap.put("Place",places);
                                hashMap.put("Description",desc);
                                hashMap.put("Location",locs);
                                hashMap.put("Counter", countAct);



                                SchedulesRef.child(SchKey).child("Activities").child("Activity: " +no)
                                        .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {

                                        if (task.isSuccessful()){

                                            dialogInterface.dismiss();
                                        }

                                    }
                                });

                            }





                        });


                        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Bye ", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();

                            }
                        });




                        mBuilder.setView(view);
                        AlertDialog dialog=mBuilder.create();
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);

                        dialog.show();



                    }
                });















    }

    private void SendUserToMainActivity() {
        Intent setupIntent2 = new Intent(SchPostsActivity.this, MainActivity.class);
        startActivity(setupIntent2);
    }


    private void DisplayAllDays() {

        DaysRef= FirebaseDatabase.getInstance().getReference().child("Schedules").child(SchKey).child("Activities");


        FirebaseRecyclerOptions<Activities> options =
                new FirebaseRecyclerOptions.Builder<Activities>()
                        .setQuery(DaysRef, Activities.class)
                        .build();



        FirebaseRecyclerAdapter<Activities, SchPostsActivity.DaysViewHolder> firebaseRecyclerAdapter2 =
                new FirebaseRecyclerAdapter<Activities, DaysViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull DaysViewHolder daysViewHolder, int i, @NonNull Activities act) {

                        daysViewHolder.acts.setText(act.getPlace());
                        daysViewHolder.days.setText("Activity: "+act.getActivity());


                    }

                    @NonNull
                    @Override
                    public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view2= LayoutInflater.from(parent.getContext()).inflate(R.layout.sch_days,parent,false);
                        SchPostsActivity.DaysViewHolder viewHolder2=new SchPostsActivity.DaysViewHolder(view2);
                        return viewHolder2;
                    }
                };


        firebaseRecyclerAdapter2.startListening();
        daysList.setAdapter(firebaseRecyclerAdapter2);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getItemAtPosition(position).equals("City")){

            Toast.makeText(this, "Please choose a city", Toast.LENGTH_SHORT).show();
        }
        else{

            loc = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public static class DaysViewHolder extends RecyclerView.ViewHolder {
        TextView days, acts;


        public DaysViewHolder(View itemView) {
            super(itemView);

            days = itemView.findViewById(R.id.all_daysno);
            acts = itemView.findViewById(R.id.all_daysact);


        }
    }





























}

