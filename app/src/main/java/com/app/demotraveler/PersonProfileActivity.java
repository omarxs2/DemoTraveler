package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonProfileActivity extends AppCompatActivity {
    private TextView UserName,UserFullName,UserStatus,UserCountry,UserGender,UserDB,UserRelationStatus;
    private CircleImageView UserProfImage;
    private Button FollowBtn, UnFollowBtn;

    private DatabaseReference UserRef,FriendReqRef,FriendsRef;
    private String SenderId,RecieverId;
    private FirebaseAuth mAuth;
    private String CurrentState;
    private String saveCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);


        mAuth = FirebaseAuth.getInstance();
        SenderId=mAuth.getCurrentUser().getUid();


        RecieverId = getIntent().getExtras().get("UserKey").toString();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendReqRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        InitializationForAllFields();




        UserRef.child(RecieverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String myProImage = dataSnapshot.child("ProfileImage").getValue().toString();
                    String myUserName = dataSnapshot.child("UserName").getValue().toString();
                    String myFullName = dataSnapshot.child("FullName").getValue().toString();
                    String myStatus = dataSnapshot.child("Bio").getValue().toString();
                    String xmeCountry = dataSnapshot.child("Country").getValue().toString();
                    String xmeGender = dataSnapshot.child("Gender").getValue().toString();
                    String xmeDOB = dataSnapshot.child("DateOfBirth").getValue().toString();
                    String xmeRelationship = dataSnapshot.child("Relationship").getValue().toString();

                    UserRelationStatus.setText(xmeRelationship);
                    UserCountry.setText(xmeCountry);
                    UserGender.setText(xmeGender);
                    UserDB.setText(xmeDOB);
                    UserFullName.setText(myFullName);
                    UserName.setText("@"+myUserName);
                    UserStatus.setText(myStatus);



                    Picasso.get().load(myProImage).placeholder(R.drawable.profile).into(UserProfImage);


                    MaintainButtons();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        UnFollowBtn.setVisibility(View.INVISIBLE);
        UnFollowBtn.setEnabled(false);

        if (!SenderId.equals(RecieverId)){

            FollowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FollowBtn.setEnabled(false);

                    if (CurrentState.equals("NotFriends")){

                        SendFriendReq();
                    }

                    if (CurrentState.equals("RequestSent")){

                        CancelFriendReq();
                    }
                    if (CurrentState.equals("RequestReceived")){

                        AcceptFriendRequest();
                    }
                    if (CurrentState.equals("Friends")){

                        DeletingFriend();
                    }


                }
            });

        } else{
            UnFollowBtn.setVisibility(View.INVISIBLE);

            FollowBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void DeletingFriend() {


        FriendsRef.child(SenderId).child(RecieverId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            FriendsRef.child(RecieverId).child(SenderId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                FollowBtn.setEnabled(true);
                                                CurrentState="NotFriends";
                                                FollowBtn.setText("Follow");
                                                UnFollowBtn.setVisibility(View.INVISIBLE);
                                                UnFollowBtn.setEnabled(false);


                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    private void AcceptFriendRequest() {


        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calDate.getTime());

        FriendsRef.child(SenderId).child(RecieverId).child("Date").setValue(saveCurrentDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FriendsRef.child(RecieverId).child(SenderId).child("Date").setValue(saveCurrentDate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                FriendReqRef.child(SenderId).child(RecieverId).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()){
                                                                    FriendReqRef.child(RecieverId).child(SenderId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()){
                                                                                        FollowBtn.setEnabled(true);
                                                                                        CurrentState="Friends";
                                                                                        FollowBtn.setText("UNFollow");
                                                                                        UnFollowBtn.setVisibility(View.INVISIBLE);
                                                                                        UnFollowBtn.setEnabled(false);


                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });





                                            }
                                        }
                                    });
                        }
                    }
                });



    }



    private void CancelFriendReq() {


        FriendReqRef.child(SenderId).child(RecieverId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            FriendReqRef.child(RecieverId).child(SenderId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                FollowBtn.setEnabled(true);
                                                CurrentState="NotFriends";
                                                FollowBtn.setText("Follow");
                                                UnFollowBtn.setVisibility(View.INVISIBLE);
                                                UnFollowBtn.setEnabled(false);


                                            }
                                        }
                                    });
                        }
                    }
                });


    }




    private void MaintainButtons() {

        FriendReqRef.child(SenderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(RecieverId)){

                    String reqType = dataSnapshot.child(RecieverId).child("RequestType").getValue().toString();

                    if (reqType.equals("sent")){
                        CurrentState = "RequestSent";
                        FollowBtn.setText("Follow requested");
                        UnFollowBtn.setVisibility(View.INVISIBLE);
                        UnFollowBtn.setEnabled(false);

                    }

                    else if (reqType.equals("received")){
                        CurrentState = "RequestReceived";
                        FollowBtn.setText("accept");
                        UnFollowBtn.setVisibility(View.VISIBLE);
                        UnFollowBtn.setEnabled(true);

                        UnFollowBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CancelFriendReq();
                            }
                        });
                    }


                }
                else{

                    FriendsRef.child(SenderId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(RecieverId)){
                                CurrentState = "Friends";
                                FollowBtn.setText("unfollow");
                                UnFollowBtn.setVisibility(View.INVISIBLE);
                                UnFollowBtn.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void SendFriendReq() {

        FriendReqRef.child(SenderId).child(RecieverId).child("RequestType").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            FriendReqRef.child(RecieverId).child(SenderId)
                                    .child("RequestType").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        FollowBtn.setEnabled(true);
                                        CurrentState="RequestSent";
                                        FollowBtn.setText("Follow requested");
                                        UnFollowBtn.setVisibility(View.INVISIBLE);
                                        UnFollowBtn.setEnabled(false);


                                    }
                                }
                            });
                        }
                    }
                });

    }


    private void InitializationForAllFields() {
        UserRelationStatus = (TextView) findViewById(R.id.person_profile_relationship);
        UserCountry = (TextView) findViewById(R.id.person_profile_country);
        UserGender = (TextView) findViewById(R.id.person_profile_gender);
        UserDB = (TextView) findViewById(R.id.person_profile_dob);
        UserName = (TextView) findViewById(R.id.person_profile_username);
        UserFullName = (TextView) findViewById(R.id.person_profile_fullname);
        UserStatus = (TextView) findViewById(R.id.person_profile_bio);


        UserProfImage = (CircleImageView) findViewById(R.id.person_profile_profileimage);

        FollowBtn = (Button) findViewById(R.id.person_profile_follow_btn);
        UnFollowBtn = (Button) findViewById(R.id.person_profile_unfollow_btn);


        CurrentState="NotFriends";



    }
}
