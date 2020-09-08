package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class DailyProgramActivity extends AppCompatActivity {

    private RecyclerView allDays;
    private DatabaseReference UserRef,SchRef;
    private FirebaseAuth mAuth;
    private String DayRef;
    private Toolbar mToolbat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_program);



        mToolbat= (Toolbar) findViewById(R.id.act_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Activities");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allDays = (RecyclerView)findViewById(R.id.all_days_recy);
        allDays.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        allDays.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();


        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");


        DayRef=  getIntent().getExtras().get("SchKey").toString();
        SchRef = FirebaseDatabase.getInstance().getReference().child("Schedules").child(DayRef).child("Activities");



        DisplayAllDays();


    }






    private void DisplayAllDays() {

        Query query = SchRef.orderByChild("Counter");

        FirebaseRecyclerOptions<Activities> options =
                new FirebaseRecyclerOptions.Builder<Activities>()
                        .setQuery(query, Activities.class)
                        .build();



        FirebaseRecyclerAdapter<Activities, DailyProgramActivity.DaysViewHolder> adapter = new FirebaseRecyclerAdapter<Activities, DaysViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DaysViewHolder daysViewHolder, int i, @NonNull Activities act) {


                daysViewHolder.head.setText("Activity "+act.getActivity()+"\n"+act.getType());
                daysViewHolder.place.setText(act.getPlace());
                daysViewHolder.address.setText(act.getLocation());
                daysViewHolder.desc.setText(act.getDescription());


            }

            @NonNull
            @Override
            public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_all_days,parent,false);
                DailyProgramActivity.DaysViewHolder viewHolder=new DailyProgramActivity.DaysViewHolder(view);
                return viewHolder;
            }
        };


        adapter.startListening();
        allDays.setAdapter(adapter);



    }




    public static class DaysViewHolder extends RecyclerView.ViewHolder {
        TextView place, address, desc,head;

        public DaysViewHolder(View itemView) {
            super(itemView);


            head = itemView.findViewById(R.id.daily_header);
            place = itemView.findViewById(R.id.day_place);
            address = itemView.findViewById(R.id.day_loc);
            desc = itemView.findViewById(R.id.day_desc);


        }
    }





}
