
package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RoomActivity extends AppCompatActivity {

    private Toolbar mToolbat;
    private ImageButton CreateBtn,Searchbtn;
    private EditText SearchInput;
    private RecyclerView AllRooms;
    private DatabaseReference RoomRef,UserRef;
    private String CurrentUserID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);



        mToolbat= (Toolbar) findViewById(R.id.room_bar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Group Trips");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        CreateBtn=findViewById(R.id.create_room_btn);
        Searchbtn=findViewById(R.id.find_room_btn);

        SearchInput=findViewById(R.id.search_room);


        AllRooms=findViewById(R.id.all_rooms_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        AllRooms.setHasFixedSize(true);
        AllRooms.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            CurrentUserID = mAuth.getCurrentUser().getUid();
        }

        RoomRef = FirebaseDatabase.getInstance().getReference().child("Rooms");

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");




        CreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToCreateRoomActivity();
            }
        });



        DisplayAllRooms();



    }






    ////----------------------/////


    private void DisplayAllRooms() {

        FirebaseRecyclerOptions<Room> options =
                new FirebaseRecyclerOptions.Builder<Room>()
                        .setQuery(RoomRef, Room.class)
                        .build();


        FirebaseRecyclerAdapter<Room,RoomsViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Room, RoomsViewHolder>(options) {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    protected void onBindViewHolder(@NonNull final RoomsViewHolder roomsViewHolder, int i, @NonNull Room room) {

                        final String PostKey = getRef(i).getKey();
                        final String Room_creater = room.getUId();

                        UserRef.child(Room_creater).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                   final String Usernane =  dataSnapshot.child("FullName").getValue().toString();
                                    final String ProfileImage =  dataSnapshot.child("ProfileImage").getValue().toString();


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





                        roomsViewHolder.rDetails.setMaxHeight(0);




                        String about = room.getDescription();
                        String city = room.getCity().toUpperCase();
                        String looking = room.getLooking();
                        String date1 = room.getDate1();
                        String date2 = room.getDate2();
                        String date = room.getCurrentDate();


                        roomsViewHolder.rAbout.setText("About the trip: "+about);
                        roomsViewHolder.rCity.setText(city);
                        roomsViewHolder.rLooking.setText("Looking for: " +looking);
                        roomsViewHolder.rDatesRang.setText(date1+ " -- "+date2);
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

                        if (food.equals("true")){

                            roomsViewHolder.tFood.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tFood.setTextColor(R.color.FontTagP);
                        }
                        if (food.equals("false")){
                            roomsViewHolder.tFood.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tFood.setTextColor(R.color.FontTagN);
                        }

                        ////



                        if (his.equals("true")){

                            roomsViewHolder.tHistory.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tHistory.setTextColor(R.color.FontTagP);
                        }
                        if (his.equals("false")){
                            roomsViewHolder.tHistory.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tHistory.setTextColor(R.color.FontTagN);
                        }




                        if (fest.equals("true")){

                            roomsViewHolder.tFestivals.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tFestivals.setTextColor(R.color.FontTagP);
                        }
                        if (fest.equals("false")){
                            roomsViewHolder.tFestivals.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tFestivals.setTextColor(R.color.FontTagN);
                        }



                        if (muse.equals("true")){

                            roomsViewHolder.tMuseums.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tMuseums.setTextColor(R.color.FontTagP);
                        }
                        if (muse.equals("false")){
                            roomsViewHolder.tMuseums.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tMuseums.setTextColor(R.color.FontTagN);
                        }





                        if (sport.equals("true")){

                            roomsViewHolder.tSport.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tSport.setTextColor(R.color.FontTagP);
                        }
                        if (sport.equals("false")){
                            roomsViewHolder.tSport.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tSport.setTextColor(R.color.FontTagN);
                        }






                        if (activ.equals("true")){

                            roomsViewHolder.tActivities.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tActivities.setTextColor(R.color.FontTagP);
                        }
                        if (activ.equals("false")){
                            roomsViewHolder.tActivities.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tActivities.setTextColor(R.color.FontTagN);
                        }






                        if (part.equals("true")){

                            roomsViewHolder.tParties.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tParties.setTextColor(R.color.FontTagP);
                        }
                        if (part.equals("false")){
                            roomsViewHolder.tParties.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tParties.setTextColor(R.color.FontTagN);
                        }






                        if (nature.equals("true")){

                            roomsViewHolder.tNature.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tNature.setTextColor(R.color.FontTagP);
                        }
                        if (nature.equals("false")){
                            roomsViewHolder.tNature.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tNature.setTextColor(R.color.FontTagN);
                        }





                        if (shopp.equals("true")){

                            roomsViewHolder.tShopping.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tShopping.setTextColor(R.color.FontTagP);
                        }
                        if (shopp.equals("false")){
                            roomsViewHolder.tShopping.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tShopping.setTextColor(R.color.FontTagN);
                        }





                        if (religi.equals("true")){

                            roomsViewHolder.tReligious.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tReligious.setTextColor(R.color.FontTagP);
                        }
                        if (religi.equals("false")){
                            roomsViewHolder.tReligious.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tReligious.setTextColor(R.color.FontTagN);
                        }





                        if (recrea.equals("true")){

                            roomsViewHolder.tRecreation.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tRecreation.setTextColor(R.color.FontTagP);
                        }
                        if (recrea.equals("false")){
                            roomsViewHolder.tRecreation.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tRecreation.setTextColor(R.color.FontTagN);
                        }





                        if (culture.equals("true")){

                            roomsViewHolder.tCulture.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tCulture.setTextColor(R.color.FontTagP);
                        }
                        if (culture.equals("false")){
                            roomsViewHolder.tCulture.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tCulture.setTextColor(R.color.FontTagN);
                        }





                        if (art.equals("true")){

                            roomsViewHolder.tArt.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tArt.setTextColor(R.color.FontTagP);
                        }
                        if (art.equals("false")){
                            roomsViewHolder.tArt.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tArt.setTextColor(R.color.FontTagN);
                        }






                        if (sea.equals("true")){

                            roomsViewHolder.tSea.setBackgroundResource(R.drawable.tags);
                            roomsViewHolder.tSea.setTextColor(R.color.FontTagP);
                        }
                        if (sea.equals("false")){
                            roomsViewHolder.tSea.setBackgroundResource(R.drawable.tagn);
                            roomsViewHolder.tSea.setTextColor(R.color.FontTagN);
                        }


                        final HashMap userMap2 = new HashMap();

                        userMap2.put("Description",about);
                        userMap2.put("City",city);
                        userMap2.put("Looking",looking);
                        userMap2.put("Date1",date1);
                        userMap2.put("Date2",date2);
                        userMap2.put("UId",Room_creater);
                        userMap2.put("CurrentDate",date);
                        userMap2.put("Food" ,food);
                        userMap2.put("History" ,his);
                        userMap2.put("Festivals" ,fest);
                        userMap2.put("Museums" ,muse);
                        userMap2.put("Sport" ,sport);
                        userMap2.put("Activities",activ);
                        userMap2.put("Party",part);
                        userMap2.put("Nature",nature);
                        userMap2.put("Shopping" ,shopp);
                        userMap2.put("Religious",religi);
                        userMap2.put("Recreation" ,recrea);
                        userMap2.put("Culture" ,culture);
                        userMap2.put("Art",art);
                        userMap2.put("Sea",sea);
                        userMap2.put("status","joined");


                        roomsViewHolder.rJoinBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                UserRef.child(CurrentUserID).child("Joined Rooms")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.child(PostKey).exists()){
                                                    Toast.makeText(RoomActivity.this, "You are Joined", Toast.LENGTH_SHORT).show();
                                                }else{


                                                    UserRef.child(Room_creater).child("My Rooms")
                                                            .child(PostKey).child("Joined People").child(CurrentUserID).child("status").setValue("joined")
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                        UserRef.child(CurrentUserID).child("Joined Rooms").child(PostKey).setValue(userMap2)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        SendUserToAllRoomsActivity();
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






                    }

                    @NonNull
                    @Override
                    public RoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_rooms_layout,parent,false);
                        RoomActivity.RoomsViewHolder viewHolder=new RoomActivity.RoomsViewHolder(view);
                        return viewHolder;
                    }
                };


        firebaseRecyclerAdapter.startListening();
        AllRooms.setAdapter(firebaseRecyclerAdapter);


    }















    public static class RoomsViewHolder extends RecyclerView.ViewHolder {
        TextView un,rdate,rCity,rDatesRang,rAbout,
                tFood,tHistory,tFestivals,tMuseums,tSport,tActivities,tParties,tNature,tShopping,
                tReligious,tRecreation,tCulture,tArt,tSea,rLooking,rDetails;
        CircleImageView UserPImg;
        Button rJoinBtn;

        public RoomsViewHolder(View itemView) {
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




    private void SendUserToAllRoomsActivity() {

        Intent mainIntent = new Intent(RoomActivity.this, JoindRoomsActivity.class);
       // mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
      //  finish();

    }



    private void SendUserToCreateRoomActivity() {
        Intent mainIntent = new Intent(RoomActivity.this, CreateRoomActivity.class);
        startActivity(mainIntent);
    }
}
