package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowRoomPeopleActivity extends AppCompatActivity {


    private RecyclerView allPeople;
    private DatabaseReference UserRef,PeopleRef;
    private FirebaseAuth mAuth;
    private String CurrentUserID;
    private Button Submitbtn;
    private EditText info;
    private String postkey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_room_people);


        allPeople = (RecyclerView)findViewById(R.id.my_room_req_recycle);
        allPeople.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        allPeople.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserID = mAuth.getCurrentUser().getUid();


        postkey  = getIntent().getExtras().get("PostKey").toString();


        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        PeopleRef= UserRef.child(CurrentUserID).child("My Rooms")
                .child(postkey).child("Joined People");

        Submitbtn = (Button) findViewById(R.id.info_sub_btn);
        info = (EditText) findViewById(R.id.room_info);



        UserRef.child(CurrentUserID).child("My Rooms")
                .child(postkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Meeting").exists()){
                     Submitbtn.setText("Submitted");
                    info.setText(dataSnapshot.child("Meeting").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DisplayAllPeople();

    }

    private void DisplayAllPeople() {


        FirebaseRecyclerOptions<People> options =
                new FirebaseRecyclerOptions.Builder<People>()
                        .setQuery(PeopleRef, People.class)
                        .build();



        FirebaseRecyclerAdapter<People, ShowRoomPeopleActivity.PeopleViewHolder> adapter =
                new FirebaseRecyclerAdapter<People, PeopleViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final PeopleViewHolder peopleViewHolder, int i, @NonNull People people) {

                        final String UID = getRef(i).getKey();

                    UserRef.child(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                String pi = dataSnapshot.child("ProfileImage").getValue().toString();
                                String name = dataSnapshot.child("FullName").getValue().toString();

                                peopleViewHolder.FullName.setText(name);
                                Picasso.get().load(pi)
                                        .placeholder(R.drawable.add_post_high)
                                        .into(peopleViewHolder.ProfileImage);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                        ////////////////////////////////////////////








                        final String stat = people.getStatus();
                        if (stat.equals("accepted")){
                            peopleViewHolder.accept.setBackgroundResource(R.drawable.button3);
                            peopleViewHolder.accept.setText("Accepted");


                        }else{
                            peopleViewHolder.accept.setBackgroundResource(R.drawable.buttons);
                            peopleViewHolder.accept.setText("Accept");


                        }




                            peopleViewHolder.accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (stat.equals("joined")){
                                    final HashMap h = new HashMap();
                                    h.put("status","accepted");

                                    PeopleRef.child(UID).setValue(h).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            UserRef.child(UID).child("Joined Rooms").child(postkey).child("status").setValue("accepted");
                                        }
                                    });
                                }
                                else{
                                    final HashMap h2 = new HashMap();
                                    h2.put("status","joined");
                                    PeopleRef.child(UID).setValue(h2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            UserRef.child(UID).child("Joined Rooms").child(postkey).child("status").setValue("joined");
                                        }
                                    });

                                }
                            }
                        });




                        Submitbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final HashMap h = new HashMap();
                                h.put("status", "accepted");


                                UserRef.child(CurrentUserID).child("My Rooms")
                                        .child(postkey).child("Meeting")
                                        .setValue(info.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        UserRef.child(UID).child("Joined Rooms").child(postkey)
                                                .child("Meeting").setValue(info.getText().toString())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(ShowRoomPeopleActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });


                            }

                        });





                    }

                    @NonNull
                    @Override
                    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_people_want_join,parent,false);
                        ShowRoomPeopleActivity.PeopleViewHolder viewHolder=new ShowRoomPeopleActivity.PeopleViewHolder(view);
                        return viewHolder;
                    }


                };

        adapter.startListening();
        allPeople.setAdapter(adapter);





    }


    public static class PeopleViewHolder extends RecyclerView.ViewHolder {
        private TextView FullName;
        private CircleImageView ProfileImage;
        private Button accept;
        private DatabaseReference UserRef;
        private String CurrentUserID;
        private FirebaseAuth mAuth;
        private String postkey;
        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);

            FullName=itemView.findViewById(R.id.all_rfullname);
            ProfileImage=itemView.findViewById(R.id.all_rpeoples_pi);
            accept=itemView.findViewById(R.id.accept_people_btn);
            UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
            mAuth = FirebaseAuth.getInstance();
            CurrentUserID = mAuth.getCurrentUser().getUid();

        }


        }



}
