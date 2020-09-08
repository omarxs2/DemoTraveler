package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class JoindRoomsActivity extends AppCompatActivity {

    private Toolbar mToolbat;
    private TabHost tabHost;
    private TabHost.TabSpec tabSpec;


    private DatabaseReference RoomRef,UserRef;
    private String CurrentUserID;
    private FirebaseAuth mAuth;
    private RecyclerView AllRooms,AllRooms2,AllWantGo;
    private ImageButton go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joind_rooms);







        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabSpec = tabHost.newTabSpec("Screen-1");
        tabSpec.setContent(R.id.my_rooms1);
        tabSpec.setIndicator("My Rooms", null);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("Screen-2");
        tabSpec.setContent(R.id.my_j_rooms);
        tabSpec.setIndicator("Joined Rooms", null);
        tabHost.addTab(tabSpec);


        mToolbat= (Toolbar) findViewById(R.id.all_rooms_join);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Rooms");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        AllRooms=findViewById(R.id.all_my_rooms);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        AllRooms.setHasFixedSize(true);
        AllRooms.setLayoutManager(linearLayoutManager);


        ///------------////

        AllRooms2=findViewById(R.id.all_my_Jrooms);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        AllRooms2.setHasFixedSize(true);
        AllRooms2.setLayoutManager(linearLayoutManager2);


        ///---------------////

        go = (ImageButton) findViewById(R.id.add_room);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToAddRoom();
            }
        });


        ///---------------////








        mAuth = FirebaseAuth.getInstance();
        CurrentUserID = mAuth.getCurrentUser().getUid();


        RoomRef = FirebaseDatabase.getInstance().getReference().child("Rooms");

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");



          DisplayAllRooms2();
        DisplayAllRooms22();
    }

    private void SendUserToAddRoom() {
        Intent mainIntent = new Intent(JoindRoomsActivity.this, CreateRoomActivity.class);
        startActivity(mainIntent);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }



    //// Show my Rooms
    private void DisplayAllRooms2() {

        //Query SortPosts = RoomRef.startAt(CurrentUserID);

        FirebaseRecyclerOptions<Room> options =
                new FirebaseRecyclerOptions.Builder<Room>()
                        .setQuery(UserRef.child(CurrentUserID).child("My Rooms"), Room.class)
                        .build();


        FirebaseRecyclerAdapter<Room, RoomsViewHolder2> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Room, RoomsViewHolder2>(options) {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    protected void onBindViewHolder(@NonNull final RoomsViewHolder2 roomsViewHolder, int i, @NonNull Room room) {

                        final String PostKey = getRef(i).getKey();
                        final String Room_creater = room.getUId();


                        roomsViewHolder.rDetails.setMaxHeight(0);




                            UserRef.child(Room_creater).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        final String Usernane = dataSnapshot.child("FullName").getValue().toString();
                                        final String ProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();


                                        roomsViewHolder.un.setText(Usernane);
                                        Picasso.get().load(ProfileImage)
                                                .placeholder(R.drawable.add_post_high)
                                                .into(roomsViewHolder.UserPImg);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            roomsViewHolder.rJoinBtn.setText("details");

                        roomsViewHolder.rJoinBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent mainIntent = new Intent(JoindRoomsActivity.this, ShowRoomPeopleActivity.class);
                                mainIntent.putExtra("PostKey",PostKey);
                                startActivity(mainIntent);

                            }
                        });


                            String about = room.getDescription();
                            String city = room.getCity().toUpperCase();
                            String looking = room.getLooking();
                            String date1 = room.getDate1();
                            String date2 = room.getDate2();
                            String date = room.getCurrentDate();


                            roomsViewHolder.rAbout.setText("About the trip: " + about);
                            roomsViewHolder.rCity.setText(city);
                            roomsViewHolder.rLooking.setText("Looking for: " + looking);
                            roomsViewHolder.rDatesRang.setText(date1 + " -- " + date2);
                            roomsViewHolder.rdate.setText(date);


                            String food = room.getFood();
                            String his = room.getHistory();
                            String fest = room.getFestivals();
                            String muse = room.getMuseums();
                            String sport = room.getSport();
                            String activ = room.getActivities();
                            String part = room.getParty();
                            String nature = room.getNature();
                            String shopp = room.getShopping();
                            String religi = room.getReligious();
                            String recrea = room.getRecreation();
                            String culture = room.getCulture();
                            String art = room.getArt();
                            String sea = room.getSea();

                            if (food.equals("true")) {

                                roomsViewHolder.tFood.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tFood.setTextColor(R.color.FontTagP);
                            }
                            if (food.equals("false")) {
                                roomsViewHolder.tFood.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tFood.setTextColor(R.color.FontTagN);
                            }

                            ////


                            if (his.equals("true")) {

                                roomsViewHolder.tHistory.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tHistory.setTextColor(R.color.FontTagP);
                            }
                            if (his.equals("false")) {
                                roomsViewHolder.tHistory.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tHistory.setTextColor(R.color.FontTagN);
                            }


                            if (fest.equals("true")) {

                                roomsViewHolder.tFestivals.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tFestivals.setTextColor(R.color.FontTagP);
                            }
                            if (fest.equals("false")) {
                                roomsViewHolder.tFestivals.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tFestivals.setTextColor(R.color.FontTagN);
                            }


                            if (muse.equals("true")) {

                                roomsViewHolder.tMuseums.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tMuseums.setTextColor(R.color.FontTagP);
                            }
                            if (muse.equals("false")) {
                                roomsViewHolder.tMuseums.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tMuseums.setTextColor(R.color.FontTagN);
                            }


                            if (sport.equals("true")) {

                                roomsViewHolder.tSport.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tSport.setTextColor(R.color.FontTagP);
                            }
                            if (sport.equals("false")) {
                                roomsViewHolder.tSport.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tSport.setTextColor(R.color.FontTagN);
                            }


                            if (activ.equals("true")) {

                                roomsViewHolder.tActivities.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tActivities.setTextColor(R.color.FontTagP);
                            }
                            if (activ.equals("false")) {
                                roomsViewHolder.tActivities.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tActivities.setTextColor(R.color.FontTagN);
                            }


                            if (part.equals("true")) {

                                roomsViewHolder.tParties.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tParties.setTextColor(R.color.FontTagP);
                            }
                            if (part.equals("false")) {
                                roomsViewHolder.tParties.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tParties.setTextColor(R.color.FontTagN);
                            }


                            if (nature.equals("true")) {

                                roomsViewHolder.tNature.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tNature.setTextColor(R.color.FontTagP);
                            }
                            if (nature.equals("false")) {
                                roomsViewHolder.tNature.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tNature.setTextColor(R.color.FontTagN);
                            }


                            if (shopp.equals("true")) {

                                roomsViewHolder.tShopping.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tShopping.setTextColor(R.color.FontTagP);
                            }
                            if (shopp.equals("false")) {
                                roomsViewHolder.tShopping.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tShopping.setTextColor(R.color.FontTagN);
                            }


                            if (religi.equals("true")) {

                                roomsViewHolder.tReligious.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tReligious.setTextColor(R.color.FontTagP);
                            }
                            if (religi.equals("false")) {
                                roomsViewHolder.tReligious.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tReligious.setTextColor(R.color.FontTagN);
                            }


                            if (recrea.equals("true")) {

                                roomsViewHolder.tRecreation.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tRecreation.setTextColor(R.color.FontTagP);
                            }
                            if (recrea.equals("false")) {
                                roomsViewHolder.tRecreation.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tRecreation.setTextColor(R.color.FontTagN);
                            }


                            if (culture.equals("true")) {

                                roomsViewHolder.tCulture.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tCulture.setTextColor(R.color.FontTagP);
                            }
                            if (culture.equals("false")) {
                                roomsViewHolder.tCulture.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tCulture.setTextColor(R.color.FontTagN);
                            }


                            if (art.equals("true")) {

                                roomsViewHolder.tArt.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tArt.setTextColor(R.color.FontTagP);
                            }
                            if (art.equals("false")) {
                                roomsViewHolder.tArt.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tArt.setTextColor(R.color.FontTagN);
                            }


                            if (sea.equals("true")) {

                                roomsViewHolder.tSea.setBackgroundResource(R.drawable.tags);
                                roomsViewHolder.tSea.setTextColor(R.color.FontTagP);
                            }
                            if (sea.equals("false")) {
                                roomsViewHolder.tSea.setBackgroundResource(R.drawable.tagn);
                                roomsViewHolder.tSea.setTextColor(R.color.FontTagN);
                            }


                      //  }



                    }

                    @NonNull
                    @Override
                    public RoomsViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_rooms_layout,parent,false);
                        RoomsViewHolder2 viewHolder=new RoomsViewHolder2(view);
                        return viewHolder;
                    }
                };


        firebaseRecyclerAdapter.startListening();
        AllRooms.setAdapter(firebaseRecyclerAdapter);


    }





    public static class RoomsViewHolder2 extends RecyclerView.ViewHolder {
        TextView un,rdate,rCity,rDatesRang,rAbout,
                tFood,tHistory,tFestivals,tMuseums,tSport,tActivities,tParties,tNature,tShopping,
                tReligious,tRecreation,tCulture,tArt,tSea,rLooking,rDetails;
        CircleImageView UserPImg;
        Button rJoinBtn;

        public RoomsViewHolder2(View itemView) {
            super(itemView);


            un = (TextView) itemView.findViewById(R.id.room_user_name);
            rdate = (TextView) itemView.findViewById(R.id.room_date);
            rCity = (TextView) itemView.findViewById(R.id.room_city);
            rDatesRang = (TextView) itemView.findViewById(R.id.room_dates);
            rAbout = (TextView) itemView.findViewById(R.id.room_about_desc);
            tFood = (TextView) itemView.findViewById(R.id.room_tfood);
            tHistory = (TextView) itemView.findViewById(R.id.room_thistory);
            tFestivals = (TextView) itemView.findViewById(R.id.room_tfestivals);
            tMuseums = (TextView) itemView.findViewById(R.id.room_tmuseums);
            tSport = (TextView) itemView.findViewById(R.id.room_tsport);
            tActivities = (TextView) itemView.findViewById(R.id.room_tactivities);
            tParties = (TextView) itemView.findViewById(R.id.room_tparties);
            tNature = (TextView) itemView.findViewById(R.id.room_tnature);
            tShopping = (TextView) itemView.findViewById(R.id.room_tshopping);
            tReligious = (TextView) itemView.findViewById(R.id.room_treligious);
            tRecreation = (TextView) itemView.findViewById(R.id.room_trecreation);
            tCulture = (TextView) itemView.findViewById(R.id.room_tculture);
            tArt = (TextView) itemView.findViewById(R.id.room_tart);
            tSea = (TextView) itemView.findViewById(R.id.room_tsea);
            rLooking = (TextView) itemView.findViewById(R.id.room_tlooking);
            rDetails = (TextView) itemView.findViewById(R.id.details_room);

            UserPImg = (CircleImageView) itemView.findViewById(R.id.room_profile_image);


            rJoinBtn = (Button) itemView.findViewById(R.id.room_tjoin_btn);



        }
    }



//////////////////-----------------------------/////////////////////////



//Show my joined rooms

    private void DisplayAllRooms22() {

        FirebaseRecyclerOptions<Room> options =
                new FirebaseRecyclerOptions.Builder<Room>()
                        .setQuery(UserRef.child(CurrentUserID).child("Joined Rooms"), Room.class)
                        .build();


        FirebaseRecyclerAdapter<Room, RoomsViewHolder22> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Room, RoomsViewHolder22>(options) {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    protected void onBindViewHolder(@NonNull final RoomsViewHolder22 roomsViewHolder, int i, @NonNull Room room) {

                        final String PostKey = getRef(i).getKey();
                        final String Room_creater = room.getUId();



                        UserRef.child(Room_creater).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    final String Usernane = dataSnapshot.child("FullName").getValue().toString();
                                    final String ProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();




                                    roomsViewHolder.un.setText(Usernane);
                                    Picasso.get().load(ProfileImage)
                                            .placeholder(R.drawable.add_post_high)
                                            .into(roomsViewHolder.UserPImg);

                                    if (dataSnapshot.child("My Rooms").child(PostKey).child("Joined People").child(CurrentUserID).exists()){
                                        if (dataSnapshot.child("My Rooms").child(PostKey).child("Meeting").exists()){
                                            String x = dataSnapshot.child("My Rooms").child(PostKey).child("Meeting").getValue().toString();
                                            roomsViewHolder.rDetails.setText(x);
                                        }


                                    }




                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                        String about = room.getDescription();
                        String city = room.getCity().toUpperCase();
                        String looking = room.getLooking();
                        String date1 = room.getDate1();
                        String date2 = room.getDate2();
                        String date = room.getCurrentDate();
                        String status = room.getStatus();
                        String info = room.getMeeting();


                        if (status.equals("joined")){
                            roomsViewHolder.rJoinBtn.setText("Waiting");
                            roomsViewHolder.rJoinBtn.setEnabled(false);
                            roomsViewHolder.rJoinBtn.setBackgroundResource(R.drawable.button3);
                            roomsViewHolder.rDetails.setMaxHeight(0);


                        }
                        else if (status.equals("accepted")) {
                            roomsViewHolder.rJoinBtn.setText("Accepted");
                            roomsViewHolder.rJoinBtn.setEnabled(true);
                            roomsViewHolder.rJoinBtn.setBackgroundResource(R.drawable.buttons);
                            roomsViewHolder.rDetails.setVisibility(View.VISIBLE);

                        }


                        roomsViewHolder.rAbout.setText("About the trip: " + about);
                        roomsViewHolder.rCity.setText(city);
                        roomsViewHolder.rLooking.setText("Looking for: " + looking);
                        roomsViewHolder.rDatesRang.setText(date1 + " -- " + date2);
                        roomsViewHolder.rdate.setText(date);


                        String food = room.getFood();
                        String his = room.getHistory();
                        String fest = room.getFestivals();
                        String muse = room.getMuseums();
                        String sport = room.getSport();
                        String activ = room.getActivities();
                        String part = room.getParty();
                        String nature = room.getNature();
                        String shopp = room.getShopping();
                        String religi = room.getReligious();
                        String recrea = room.getRecreation();
                        String culture = room.getCulture();
                        String art = room.getArt();
                        String sea = room.getSea();

                        if (food.equals("true")) {

                            roomsViewHolder.tFood.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tFood.setTextColor(R.color.FontTagP);
                        }
                        if (food.equals("false")) {
                            roomsViewHolder.tFood.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tFood.setTextColor(R.color.FontTagN);
                        }

                        ////


                        if (his.equals("true")) {

                            roomsViewHolder.tHistory.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tHistory.setTextColor(R.color.FontTagP);
                        }
                        if (his.equals("false")) {
                            roomsViewHolder.tHistory.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tHistory.setTextColor(R.color.FontTagN);
                        }


                        if (fest.equals("true")) {

                            roomsViewHolder.tFestivals.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tFestivals.setTextColor(R.color.FontTagP);
                        }
                        if (fest.equals("false")) {
                            roomsViewHolder.tFestivals.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tFestivals.setTextColor(R.color.FontTagN);
                        }


                        if (muse.equals("true")) {

                            roomsViewHolder.tMuseums.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tMuseums.setTextColor(R.color.FontTagP);
                        }
                        if (muse.equals("false")) {
                            roomsViewHolder.tMuseums.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tMuseums.setTextColor(R.color.FontTagN);
                        }


                        if (sport.equals("true")) {

                            roomsViewHolder.tSport.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tSport.setTextColor(R.color.FontTagP);
                        }
                        if (sport.equals("false")) {
                            roomsViewHolder.tSport.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tSport.setTextColor(R.color.FontTagN);
                        }


                        if (activ.equals("true")) {

                            roomsViewHolder.tActivities.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tActivities.setTextColor(R.color.FontTagP);
                        }
                        if (activ.equals("false")) {
                            roomsViewHolder.tActivities.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tActivities.setTextColor(R.color.FontTagN);
                        }


                        if (part.equals("true")) {

                            roomsViewHolder.tParties.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tParties.setTextColor(R.color.FontTagP);
                        }
                        if (part.equals("false")) {
                            roomsViewHolder.tParties.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tParties.setTextColor(R.color.FontTagN);
                        }


                        if (nature.equals("true")) {

                            roomsViewHolder.tNature.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tNature.setTextColor(R.color.FontTagP);
                        }
                        if (nature.equals("false")) {
                            roomsViewHolder.tNature.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tNature.setTextColor(R.color.FontTagN);
                        }


                        if (shopp.equals("true")) {

                            roomsViewHolder.tShopping.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tShopping.setTextColor(R.color.FontTagP);
                        }
                        if (shopp.equals("false")) {
                            roomsViewHolder.tShopping.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tShopping.setTextColor(R.color.FontTagN);
                        }


                        if (religi.equals("true")) {

                            roomsViewHolder.tReligious.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tReligious.setTextColor(R.color.FontTagP);
                        }
                        if (religi.equals("false")) {
                            roomsViewHolder.tReligious.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tReligious.setTextColor(R.color.FontTagN);
                        }


                        if (recrea.equals("true")) {

                            roomsViewHolder.tRecreation.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tRecreation.setTextColor(R.color.FontTagP);
                        }
                        if (recrea.equals("false")) {
                            roomsViewHolder.tRecreation.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tRecreation.setTextColor(R.color.FontTagN);
                        }


                        if (culture.equals("true")) {

                            roomsViewHolder.tCulture.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tCulture.setTextColor(R.color.FontTagP);
                        }
                        if (culture.equals("false")) {
                            roomsViewHolder.tCulture.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tCulture.setTextColor(R.color.FontTagN);
                        }


                        if (art.equals("true")) {

                            roomsViewHolder.tArt.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tArt.setTextColor(R.color.FontTagP);
                        }
                        if (art.equals("false")) {
                            roomsViewHolder.tArt.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tArt.setTextColor(R.color.FontTagN);
                        }


                        if (sea.equals("true")) {

                            roomsViewHolder.tSea.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tSea.setTextColor(R.color.FontTagP);
                        }
                        if (sea.equals("false")) {
                            roomsViewHolder.tSea.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tSea.setTextColor(R.color.FontTagN);
                        }





                        roomsViewHolder.rJoinBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public RoomsViewHolder22 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_rooms_layout,parent,false);
                        RoomsViewHolder22 viewHolder=new RoomsViewHolder22(view);
                        return viewHolder;
                    }
                };


        firebaseRecyclerAdapter.startListening();
        AllRooms2.setAdapter(firebaseRecyclerAdapter);


    }





    public static class RoomsViewHolder22 extends RecyclerView.ViewHolder {
        TextView un,rdate,rCity,rDatesRang,rAbout,
                tFood,tHistory,tFestivals,tMuseums,tSport,tActivities,tParties,tNature,tShopping,
                tReligious,tRecreation,tCulture,tArt,tSea,rLooking,rDetails;
        CircleImageView UserPImg;
        Button rJoinBtn;

        public RoomsViewHolder22(View itemView) {
            super(itemView);


            un = (TextView) itemView.findViewById(R.id.room_user_name);
            rdate = (TextView) itemView.findViewById(R.id.room_date);
            rCity = (TextView) itemView.findViewById(R.id.room_city);
            rDatesRang = (TextView) itemView.findViewById(R.id.room_dates);
            rAbout = (TextView) itemView.findViewById(R.id.room_about_desc);
            tFood = (TextView) itemView.findViewById(R.id.room_tfood);
            tHistory = (TextView) itemView.findViewById(R.id.room_thistory);
            tFestivals = (TextView) itemView.findViewById(R.id.room_tfestivals);
            tMuseums = (TextView) itemView.findViewById(R.id.room_tmuseums);
            tSport = (TextView) itemView.findViewById(R.id.room_tsport);
            tActivities = (TextView) itemView.findViewById(R.id.room_tactivities);
            tParties = (TextView) itemView.findViewById(R.id.room_tparties);
            tNature = (TextView) itemView.findViewById(R.id.room_tnature);
            tShopping = (TextView) itemView.findViewById(R.id.room_tshopping);
            tReligious = (TextView) itemView.findViewById(R.id.room_treligious);
            tRecreation = (TextView) itemView.findViewById(R.id.room_trecreation);
            tCulture = (TextView) itemView.findViewById(R.id.room_tculture);
            tArt = (TextView) itemView.findViewById(R.id.room_tart);
            tSea = (TextView) itemView.findViewById(R.id.room_tsea);
            rLooking = (TextView) itemView.findViewById(R.id.room_tlooking);
            rDetails = (TextView) itemView.findViewById(R.id.details_room);

            UserPImg = (CircleImageView) itemView.findViewById(R.id.room_profile_image);


            rJoinBtn = (Button) itemView.findViewById(R.id.room_tjoin_btn);



        }
    }
















}
