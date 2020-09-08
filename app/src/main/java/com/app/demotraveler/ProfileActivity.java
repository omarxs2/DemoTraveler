package com.app.demotraveler;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private ImageButton EditProbtn;
    private Button FollowBtn , CreateBussAcc,BookBtn;

    private TextView UserName,UserFullName,UserStatus,
            UserCountry,UserGender,UserDB,
            UserRelationStatus,UserNickname,SeeAllPosts
            ,FollowersView,FollowingView,PostsView,SeeReviwes;

    private CircleImageView UserProfImage;

    private DatabaseReference ProfRef,FollowingRef,FollowersRef,PostsRef,LocalRef;
    private FirebaseAuth mAuth;
    private String curentUserID,AccountTyp,CurrentState,VisitedUserId ,UsedID;

    private BottomNavigationView bottomNavView;
    private TabHost tabHost;
    private ImageView TitleImage;
    private RelativeLayout R1,R2,R3,R4,R5;
    private TextView T1,T2,T3,T4,T5;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec("Screen-1");
        tabSpec.setContent(R.id.user_info);
        tabSpec.setIndicator("About", null);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("Screen-2");
        tabSpec.setContent(R.id.business_info);
        tabSpec.setIndicator("Business", null);
        tabHost.addTab(tabSpec);




        bottomNavView = (BottomNavigationView)findViewById(R.id.profile_page_bottombar);

        bottomNavView.setSelectedItemId(R.id.profile_icon_btn);

        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                BottomItemSelector(menuItem);
                return true;
            }
        });




        BookBtn = findViewById(R.id.book_btn);



        SeeReviwes = (TextView) findViewById(R.id.see_rev);
        CreateBussAcc = (Button) findViewById(R.id.create_buss_acc);
        FollowBtn = (Button) findViewById(R.id.profile_ac_follow_btn);
        UserName = (TextView) findViewById(R.id.profile_ac_username);
        UserFullName = (TextView) findViewById(R.id.profile_ac_fullname);
        UserStatus = (TextView) findViewById(R.id.profile_ac_bio);
        UserProfImage = (CircleImageView) findViewById(R.id.profile_ac_profileimage);
        EditProbtn = (ImageButton) findViewById(R.id.profile_edit_btn);
        UserRelationStatus = (TextView) findViewById(R.id.profile_ac_relationship);
        UserCountry = (TextView) findViewById(R.id.profile_ac_country);
        UserGender = (TextView) findViewById(R.id.profile_ac_gender);
        UserDB = (TextView) findViewById(R.id.profile_ac_dob);
        UserNickname = (TextView) findViewById(R.id.nickname);
        TitleImage = (ImageView) findViewById(R.id.imagetitle);
        SeeAllPosts= (TextView) findViewById(R.id.profile_see_all_posts);

        FollowersView= (TextView) findViewById(R.id.followers_number);
        FollowingView= (TextView) findViewById(R.id.following_number);
        PostsView= (TextView) findViewById(R.id.posts_number);


        mAuth=FirebaseAuth.getInstance();
        curentUserID=mAuth.getCurrentUser().getUid();

        ProfRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FollowingRef = FirebaseDatabase.getInstance().getReference().child("Following");
        FollowersRef = FirebaseDatabase.getInstance().getReference().child("Followers");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        CurrentState = "NotFollowing";


        EditProbtn.setVisibility(View.INVISIBLE);
        FollowBtn.setVisibility(View.INVISIBLE);




        //entering the profile from bottom bar activity
        //so the intent extra will be null

        if (getIntent().getExtras() ==null){
            UsedID=curentUserID;

            BookBtn.setVisibility(View.INVISIBLE);


            EditProbtn.setVisibility(View.VISIBLE);
            FollowBtn.setVisibility(View.VISIBLE);
            FollowBtn.setText("Edit Profile");
            FollowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendUserToSettingsActivity();
                }
            });

            ProfRef.child(UsedID).child("Business Information").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.exists()){
                        CreateBussAcc.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        //entering the profile from find friends activity:
        else{
            CreateBussAcc.setVisibility(View.INVISIBLE);
            UsedID=  getIntent().getExtras().get("UserKey").toString();
            VisitedUserId  = getIntent().getExtras().get("UserKey").toString();
            //if the current user tries to enter his account from find friend activity
            if (curentUserID.equals(VisitedUserId)){
                EditProbtn.setVisibility(View.VISIBLE);
                FollowBtn.setVisibility(View.VISIBLE);
                FollowBtn.setText("Edit Profile");
                FollowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendUserToSettingsActivity();
                    }
                });

            }

            //if current user is not the same with the visited pro
            else {
                FollowBtn.setVisibility(View.VISIBLE);

                FollowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CurrentState.equals("NotFollowing")){
                            SendFollow();
                        }
                        if (CurrentState.equals("Following")){
                            CancelFollow();
                        }
                    }
                });

            }
        }




        ProfRef.child(UsedID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String myProImage = dataSnapshot.child("ProfileImage").getValue().toString();
                    String myUserName = dataSnapshot.child("UserName").getValue().toString();
                    String myFullName = dataSnapshot.child("FullName").getValue().toString();
                    String myStatus = dataSnapshot.child("Bio").getValue().toString();
                    String xmeTitleImage = dataSnapshot.child("TitleImage").getValue().toString();
                    String xmeNickname = dataSnapshot.child("Nickname").getValue().toString();
                    String xmeCountry = dataSnapshot.child("Country").getValue().toString();
                    String xmeGender = dataSnapshot.child("Gender").getValue().toString();
                    String xmeDOB = dataSnapshot.child("DateOfBirth").getValue().toString();
                    String xmeRelationship = dataSnapshot.child("Relationship").getValue().toString();


                    AccountTyp = dataSnapshot.child("AccountType").getValue().toString();



                    UserRelationStatus.setText(xmeRelationship);
                    UserCountry.setText(xmeCountry);
                    UserGender.setText(xmeGender);
                    UserDB.setText(xmeDOB);
                    UserNickname.setText(xmeNickname);

                    UserFullName.setText(myFullName);
                    UserName.setText("@"+myUserName);
                    UserStatus.setText(myStatus);


                    Picasso.get().load(myProImage).placeholder(R.drawable.profile).into(UserProfImage);



                    if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_1.png?alt=media&token=c648401a-b0cf-4b80-a328-4173440d28eb")){
                        TitleImage.setImageResource(R.drawable.image_1);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_2.png?alt=media&token=c5d467d9-845e-4477-bb83-b05157c6af52")){
                        TitleImage.setImageResource(R.drawable.image_2);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_3.png?alt=media&token=e553bd51-8350-4749-84ce-1a20a73329b9")){
                        TitleImage.setImageResource(R.drawable.image_3);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_4.png?alt=media&token=4e792030-a7e7-4495-a25b-5a3ae5bb6a30")){
                        TitleImage.setImageResource(R.drawable.image_4);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_5.png?alt=media&token=efb57363-798b-4a7e-a56f-031bed0296f8")){
                        TitleImage.setImageResource(R.drawable.image_5);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_6.png?alt=media&token=f1beb07c-3280-4377-8fde-53cf42c5936d")){
                        TitleImage.setImageResource(R.drawable.image_6);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_7.png?alt=media&token=78c3edb5-bdee-4061-90b1-48190f9fb739")){
                        TitleImage.setImageResource(R.drawable.image_7);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_8.png?alt=media&token=760de7c3-4012-44dc-8ccb-8bdcae13eb14")){
                        TitleImage.setImageResource(R.drawable.image_8);
                    }
                    else{
                        TitleImage.setImageResource(R.drawable.image_9);
                    }


                    ProfRef.child(UsedID).child("Followers").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){
                                long followers = dataSnapshot.getChildrenCount();
                                FollowersView.setText(Long.toString(followers));

                            }
                            else{
                                FollowersView.setText("0");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    ProfRef.child(UsedID).child("Following").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){
                                long following = dataSnapshot.getChildrenCount();
                                FollowingView.setText(Long.toString(following));

                            }
                            else{
                                FollowingView.setText("0");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    PostsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int counter=0;
                            if (dataSnapshot.exists()){


                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){

                                    if (UsedID.equals(childDataSnapshot.child("uId").getValue().toString())){
                                        counter++;

                                    }

                                }

                                PostsView.setText(Integer.toString(counter));

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    MaintainButtons();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        ProfRef.child(UsedID).child("Business Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){
                    SeeReviwes.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        CreateBussAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Terms and Conditions ");
                final TextView textView2 = new TextView(ProfileActivity.this);
                textView2.setText(

                        "1- Contractual Relationship:" +
                        "These Terms of Use (\"Terms\") govern your access or use.                                                                          " +
                                "                                                                                                                          "
                        +

                        "2- Arbitration Agreement:" +
                        "And possessions, of the applications, websites, content, products, and services.                                           "

                        +
                        "3- The Services:" +
                        "The Services may be made available or accessed in connection with third party services and content.                                                              "
                        +


                        "4- Access and Use of the Services:"
                                +
                                "In order to use most aspects of the Services, you must register for and maintain an active personal user Services account.                                                              "

                );


                builder.setView(textView2);


                builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intentx = new Intent(ProfileActivity.this, SettingsActivity.class);
                        startActivity(intentx);
                        intentx.putExtra("Code","yes");
                        intentx.putExtra("Agreement","yes");

                        startActivity(intentx);

                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });




                Dialog dialog =  builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);
















            }
        });







        FollowingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToFollowingActivity();

            }
        });

        FollowersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToFollowersActivity();

            }
        });


        EditProbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToSettingsActivity();
            }
        });




        SeeAllPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AccountTyp.equals("Private")){

                    Toast.makeText(ProfileActivity.this,
                            "Account is private", Toast.LENGTH_SHORT).show();
                }
                else{
                    SendUserToCurrentUserPostsActivity();
                }
            }
        });




        R1 = findViewById(R.id.firstL);
        R2 = findViewById(R.id.secondL);
        R3 = findViewById(R.id.theirdL);
        R4 = findViewById(R.id.fourthL);
        R5 = findViewById(R.id.fifthL);

        T1 = findViewById(R.id.w1);
        T2 = findViewById(R.id.w2);
        T3 = findViewById(R.id.w3);
        T4 = findViewById(R.id.w4);
        T5 = findViewById(R.id.w5);



        ProfRef.child(UsedID).child("Business Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){


                    R1.setVisibility(View.INVISIBLE);
                    R2.setVisibility(View.INVISIBLE);
                    R3.setVisibility(View.INVISIBLE);
                    R4.setVisibility(View.INVISIBLE);
                    R5.setVisibility(View.INVISIBLE);
                    BookBtn.setVisibility(View.INVISIBLE);

                }
                else{

                    String cities = dataSnapshot.child("Cities").getValue().toString();
                    String activities = dataSnapshot.child("Activities").getValue().toString();
                    String languages = dataSnapshot.child("Languages").getValue().toString();
                    String show = dataSnapshot.child("Will Show").getValue().toString();
                    String price = dataSnapshot.child("Price").getValue().toString();

                    String s = price + "$ / Day" ;

                    T1.setText(languages);
                    T2.setText(cities);
                    T3.setText(show);
                    T4.setText(activities);
                    T5.setText(s);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void openDialog() {



    }

    private void SendUserToFollowersActivity() {
        Intent mainIntent2 = new Intent(ProfileActivity.this, FollowersActivity.class);
        mainIntent2.putExtra("ID",UsedID);
        startActivity(mainIntent2);
    }

    private void MaintainButtons() {

        ProfRef.child(curentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (VisitedUserId !=null){


                    if (dataSnapshot.child("Following").child(VisitedUserId).exists()){

                        CurrentState = "Following";
                        FollowBtn.setText("Following");


                    }
                    else{
                        if (!curentUserID.equals(VisitedUserId)){
                            CurrentState = "NotFollowing";
                            FollowBtn.setText("Follow");
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendFollow() {
        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDate.format(calDate.getTime());



        ProfRef.child(curentUserID).child("Following").child(VisitedUserId).child("Date").setValue(saveCurrentDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            ProfRef.child(VisitedUserId).child("Followers").child(curentUserID).child("Date").setValue(saveCurrentDate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(ProfileActivity.this, "Followed Successfully", Toast.LENGTH_SHORT).show();
                                                CurrentState="Following";
                                                FollowBtn.setText("following");
                                            }

                                        }
                                    });
                        }

                    }
                });


        FollowingRef.child(curentUserID).child(VisitedUserId).child("Date").setValue(saveCurrentDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            FollowersRef.child(VisitedUserId).child(curentUserID).child("Date").setValue(saveCurrentDate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                             //   Toast.makeText(ProfileActivity.this, "Followed Successfully", Toast.LENGTH_SHORT).show();
                                            //    CurrentState="Following";
                                             //   FollowBtn.setText("following");
                                            }

                                        }
                                    });
                        }

                    }
                });


    }

    private void CancelFollow() {



        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Be Careful");

        final TextView textView = new TextView(ProfileActivity.this);
        textView.setText("Are you sure you want to unfollow this person?");
        builder.setView(textView);



        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ProfRef.child(curentUserID).child("Following").child(VisitedUserId).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    ProfRef.child(VisitedUserId).child("Followers").child(curentUserID).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(ProfileActivity.this, "UnFollowed", Toast.LENGTH_SHORT).show();
                                                        CurrentState="NotFollowing";
                                                        FollowBtn.setText("follow");
                                                    }

                                                }
                                            });
                                }

                            }
                        });


                FollowingRef.child(curentUserID).child(VisitedUserId).child("Date").removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    FollowersRef.child(VisitedUserId).child(curentUserID).child("Date").removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        Dialog dialog =  builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);



    }

    private void BottomItemSelector(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home_icon_btn:
                SendUserToMainActivity();
                Toast.makeText(ProfileActivity.this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.add_icon_btn:
                SendUserToPostsActivity();
                Toast.makeText(ProfileActivity.this, "Posts", Toast.LENGTH_SHORT).show();
                break;

            case R.id.services_icon_btn:
                SendUserToMainServicesActivity();
                break;

            case R.id.profile_icon_btn:
                SendUserToProfileActivity();
                Toast.makeText(ProfileActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    private void SendUserToFollowingActivity() {
        Intent mainIntent2 = new Intent(ProfileActivity.this, FollowingActivity.class);
        mainIntent2.putExtra("ID",UsedID);
        startActivity(mainIntent2);
    }

    private void SendUserToCurrentUserPostsActivity() {
        Intent mainIntent2 = new Intent(ProfileActivity.this, CurrentUserPostsActivity.class);
        startActivity(mainIntent2);


    }

    private void SendUserToProfileActivity() {
        Intent mainIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }

    private void SendUserToPostsActivity() {
        Intent setupIntent2 = new Intent(ProfileActivity.this, PostsActivity.class);
        startActivity(setupIntent2);

    }

    private void SendUserToMainActivity() {
        Intent setupIntent2 = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(setupIntent2);

    }

    private void SendUserToSettingsActivity() {
        Intent setupIntent2 = new Intent(ProfileActivity.this, SettingsActivity.class);
        startActivity(setupIntent2);
    }

    private void SendUserToMainServicesActivity() {
        Intent setupIntent2 = new Intent(ProfileActivity.this, MainServicesActivity.class);
        startActivity(setupIntent2);
    }


}
