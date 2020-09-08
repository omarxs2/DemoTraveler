package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

public class AllGoingActivity extends AppCompatActivity {

    private RecyclerView allGoing;
    private DatabaseReference UserRef,IsGoing;
    private FirebaseAuth mAuth;
    String CurrentUserID,PK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_going);


        allGoing = (RecyclerView)findViewById(R.id.all_going_list_recy);
        allGoing.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        allGoing.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
      //  CurrentUserID = getIntent().getExtras().get("ID").toString();

        PK =  getIntent().getExtras().get("PK").toString();


        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        IsGoing = FirebaseDatabase.getInstance().getReference().child("IsGoing").child(PK);





       DisplayAllGoing();


    }

    private void DisplayAllGoing() {



        FirebaseRecyclerOptions<Following> options =
                new FirebaseRecyclerOptions.Builder<Following>()
                        .setQuery(IsGoing, Following.class)
                        .build();


        FirebaseRecyclerAdapter<Following, AllGoingActivity.FollowingViewHolder> adapter = new FirebaseRecyclerAdapter<Following, AllGoingActivity.FollowingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AllGoingActivity.FollowingViewHolder holder, int position, @NonNull Following model) {

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
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }

            @NonNull
            @Override
            public AllGoingActivity.FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_following_users_layout,viewGroup,false);
                AllGoingActivity.FollowingViewHolder viewHolder=new AllGoingActivity.FollowingViewHolder(view);
                return viewHolder;
            }
        };

        if (adapter != null){
            adapter.startListening();
        }
        allGoing.setAdapter(adapter);



    }





    public static class FollowingViewHolder extends RecyclerView.ViewHolder {
        private TextView FullName,Bio;
        private CircleImageView ProfileImage;
        private Button SendMessage;

        public FollowingViewHolder(@NonNull View itemView) {
            super(itemView);
            FullName=itemView.findViewById(R.id.all_following_fullname);
            ProfileImage=itemView.findViewById(R.id.all_following_profile_image);
            SendMessage=itemView.findViewById(R.id.all_users_send_message);
            Bio=itemView.findViewById(R.id.all_following_bio);


        }
    }
}
