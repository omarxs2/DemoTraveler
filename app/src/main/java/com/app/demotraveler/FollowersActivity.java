package com.app.demotraveler;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowersActivity extends AppCompatActivity {
    private RecyclerView allFollowing;
    private DatabaseReference UserRef,FollowersRef;
    private FirebaseAuth mAuth;
    private String CurrentUserID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);




        allFollowing = (RecyclerView)findViewById(R.id.all_followers_list_recy);
        allFollowing.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        allFollowing.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserID = getIntent().getExtras().get("ID").toString();


        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FollowersRef = FirebaseDatabase.getInstance().getReference().child("Followers").child(CurrentUserID);





        DisplayAllFollowing();






    }














    private void DisplayAllFollowing() {


        FirebaseRecyclerOptions<Followers> options =
                new FirebaseRecyclerOptions.Builder<Followers>()
                        .setQuery(FollowersRef, Followers.class)
                        .build();


        FirebaseRecyclerAdapter<Followers, FollowersActivity.FollowersViewHolder> adapter = new FirebaseRecyclerAdapter<Followers, FollowersActivity.FollowersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FollowersActivity.FollowersViewHolder holder, int position, @NonNull Followers model) {

                final String UsersIDs= getRef(position).getKey();

                UserRef.child(UsersIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            final String fullname = dataSnapshot.child("FullName").getValue().toString();
                            final String image = dataSnapshot.child("ProfileImage").getValue().toString();
                            final String bio = dataSnapshot.child("Bio").getValue().toString();


                            holder.Bio.setText(bio);

                            holder.FullName.setText(fullname);



                            Picasso.get().load(image).placeholder(R.drawable.profile).into(holder.ProfileImage);



                            holder.ProfileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String visit_user_id = UsersIDs;

                                    Intent mainIntent23 = new Intent(FollowersActivity.this, ProfileActivity.class);
                                    mainIntent23.putExtra("UserKey",visit_user_id);
                                    startActivity(mainIntent23);
                                }
                            });


                            holder.FullName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String visit_user_id = UsersIDs;

                                    Intent mainIntent23 = new Intent(FollowersActivity.this, ProfileActivity.class);
                                    mainIntent23.putExtra("UserKey",visit_user_id);
                                    startActivity(mainIntent23);
                                }
                            });


                            holder.SendMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String visit_user_id = UsersIDs;
                                    Intent mainIntent23 = new Intent(FollowersActivity.this, ChatActivity.class);
                                    mainIntent23.putExtra("UserKey",visit_user_id);
                                    mainIntent23.putExtra("FullName",fullname);

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

            @NonNull
            @Override
            public FollowersActivity.FollowersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_following_users_layout,viewGroup,false);
                FollowersActivity.FollowersViewHolder viewHolder=new FollowersActivity.FollowersViewHolder(view);
                return viewHolder;
            }
        };

        if (adapter != null){
            adapter.startListening();
        }
        allFollowing.setAdapter(adapter);

    }





    public static class FollowersViewHolder extends RecyclerView.ViewHolder {
        private TextView FullName,Bio;
        private CircleImageView ProfileImage;
        private Button SendMessage;


        public FollowersViewHolder(@NonNull View itemView) {
            super(itemView);
            FullName=itemView.findViewById(R.id.all_following_fullname);
            ProfileImage=itemView.findViewById(R.id.all_following_profile_image);
            SendMessage=itemView.findViewById(R.id.all_users_send_message);
            Bio=itemView.findViewById(R.id.all_following_bio);


        }
    }













}
