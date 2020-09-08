package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AllMasagesActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private DatabaseReference UserRef,MassagesRef;
    private FirebaseAuth mAuth;
    String CurrentUserID;
    private ImageView UnreadDot;
    private RecyclerView allMassages;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_masages);

        mToolbar=(Toolbar) findViewById(R.id.all_mass_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Massages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UnreadDot = findViewById(R.id.unread_massage);

        allMassages = (RecyclerView)findViewById(R.id.all_massages__list);
        allMassages.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        allMassages.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserID = mAuth.getCurrentUser().getUid();


        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");


        MassagesRef = FirebaseDatabase.getInstance().getReference().child("Massages").child(CurrentUserID);



        DisplayAllFollowing();

    }





    private void DisplayAllFollowing() {



        FirebaseRecyclerOptions<AllMassages> options =
                new FirebaseRecyclerOptions.Builder<AllMassages>()
                        .setQuery(MassagesRef, AllMassages.class)
                        .build();


        FirebaseRecyclerAdapter<AllMassages, AllMasagesActivity.MassagesViewHolder>
                adapter = new FirebaseRecyclerAdapter<AllMassages, AllMasagesActivity.MassagesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AllMasagesActivity.MassagesViewHolder massagesViewHolder, int i, @NonNull AllMassages allMassages) {

                final String UsersIDs = getRef(i).getKey();


                if (!UsersIDs.equals(CurrentUserID)){

                    MassagesRef.child(UsersIDs).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot child: dataSnapshot.getChildren()) {


                                final String time = dataSnapshot.child(child.getKey()).child("Time").getValue().toString();
                                final String lastmass = dataSnapshot.child(child.getKey()).child("Massage").getValue().toString();

                                massagesViewHolder.MassTime.setText(time);
                                massagesViewHolder.LastMassage.setText(lastmass);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                    UserRef.child(UsersIDs).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                                final String fullname = dataSnapshot.child("FullName").getValue().toString();
                                final String image = dataSnapshot.child("ProfileImage").getValue().toString();


                                massagesViewHolder.FullName.setText(fullname);
                                Picasso.get().load(image).placeholder(R.drawable.profile).into(massagesViewHolder.ProfImage);


                                massagesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String visit_user_id = UsersIDs;
                                        Intent mainIntent23 = new Intent(AllMasagesActivity.this, ChatActivity.class);
                                        mainIntent23.putExtra("UserKey", visit_user_id);
                                        mainIntent23.putExtra("FullName", fullname);
                                        startActivity(mainIntent23);




                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            }

            }

            @NonNull
            @Override
            public AllMasagesActivity.MassagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_massages_layout,parent,false);
                AllMasagesActivity.MassagesViewHolder viewHolder=new AllMasagesActivity.MassagesViewHolder(view);
                return viewHolder;
            }
        };


        if (adapter != null){
            adapter.startListening();
        }
        allMassages.setAdapter(adapter);


        }







    public static class MassagesViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView ProfImage;
        private TextView FullName, LastMassage,MassTime;

        public MassagesViewHolder(View itemView) {
            super(itemView);


            MassTime = itemView.findViewById(R.id.massage_time);
            FullName = itemView.findViewById(R.id.massage_fullname);
            LastMassage = itemView.findViewById(R.id.massage_last_mass);
            ProfImage = itemView.findViewById(R.id.massage_image);

        }
    }

























}
