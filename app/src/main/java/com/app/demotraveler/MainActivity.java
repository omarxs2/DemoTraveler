package com.app.demotraveler;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postList,postList2,postList3;
    private Toolbar mToolbat;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,PostsRef,LikesRef,CommentsRef,GoinigRef,IsGoing,QuestLikesRef,ScheduleRef;
    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;
    String CurrentUserID;
    private BottomNavigationView bottomNavView;
    private ImageButton editImage,messagebtn;
    boolean LikeChecker = false;
    boolean GoingChecker = false;

    private int totalnolikes=0;
    private int totalgoing=0;

    private TabHost tabHost;
    private TabHost.TabSpec tabSpec;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabSpec = tabHost.newTabSpec("Screen-1");
        tabSpec.setContent(R.id.main_act1);
        tabSpec.setIndicator("Posts", null);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("Screen-2");
        tabSpec.setContent(R.id.main_act2);
        tabSpec.setIndicator("Schedules", null);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("Screen-3");
        tabSpec.setContent(R.id.main_act3);
        tabSpec.setIndicator("Who is going", null);
        tabHost.addTab(tabSpec);



        /////////////////////////

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            CurrentUserID = mAuth.getCurrentUser().getUid();
        }


        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        messagebtn = (ImageButton) findViewById(R.id.message_btn);
        LikesRef= FirebaseDatabase.getInstance().getReference().child("Likes");
        QuestLikesRef= FirebaseDatabase.getInstance().getReference().child("QuestionsLikes");
        ScheduleRef= FirebaseDatabase.getInstance().getReference().child("Schedules");

        CommentsRef= FirebaseDatabase.getInstance().getReference().child("Comments");
        GoinigRef = FirebaseDatabase.getInstance().getReference().child("WhoGoing");
        IsGoing= FirebaseDatabase.getInstance().getReference().child("IsGoing");



        mToolbat= (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("TRAVELER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postList = (RecyclerView)findViewById(R.id.all_users_posts_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);


        postList2 = (RecyclerView)findViewById(R.id.all_users_qa_list);
        postList2.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setReverseLayout(true);
        linearLayoutManager2.setStackFromEnd(true);
        postList2.setLayoutManager(linearLayoutManager2);


        postList3 = (RecyclerView)findViewById(R.id.all_users_schedules_list);
        postList3.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setReverseLayout(true);
        linearLayoutManager3.setStackFromEnd(true);
        postList3.setLayoutManager(linearLayoutManager3);




        navigationView = (NavigationView)findViewById(R.id.navigation_view);


        bottomNavView = (BottomNavigationView)findViewById(R.id.bottom_nav);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawable_view);

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout
                ,R.string.drawer_open,R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();








        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileImage=navView.findViewById(R.id.nav_profile_image);
        NavProfileUserName=navView.findViewById(R.id.nav_user_full_name);

        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                BottomItemSelector(menuItem);
                return true;
            }
        });


        if (mAuth.getCurrentUser() != null) {
            UserRef.child(CurrentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()) {


                        if (dataSnapshot.hasChild("FullName")) {
                            String fullname = dataSnapshot.child("FullName").getValue().toString();

                            NavProfileUserName.setText(fullname);
                        }

                        if (dataSnapshot.hasChild("ProfileImage")) {
                            String image = dataSnapshot.child("ProfileImage").getValue().toString();


                            Picasso.get().load(image).placeholder(R.drawable.profile).into(NavProfileImage);

                        } else {
                            Toast.makeText(MainActivity.this, "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }





        messagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToAllMassagesActivity();
            }
        });



        UserRef.child(CurrentUserID).child("LikedPosts").child("Total Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    totalnolikes = Integer.parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DisplayAllUsersPosts();
        DisplayAllGoing();
        DisplayAllSchedules();

    }




    private void DisplayAllUsersPosts() {


        Query SortPosts = PostsRef.orderByChild("Counter");

        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                        .setQuery(SortPosts, Posts.class)
                        .build();



        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostsViewHolder holder, final int position, @NonNull Posts model) {


                        final String PostKey = getRef(position).getKey();
                        final String visit_user_id = model.getuId();


                        if (CurrentUserID.equals(model.getuId())){

                            holder.editImage.setVisibility(VISIBLE);

                            holder.editImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent setupIntent2 = new Intent(MainActivity.this, ClickPostActivity.class);
                                    setupIntent2.putExtra("Post Key",PostKey);
                                    startActivity(setupIntent2);

                                }
                            });

                        }




                        holder.SetLikeButtonStatus(PostKey);
                        holder.username.setText(model.getFullName());
                        holder.time.setText(model.getTime());
                        holder.date.setText(model.getDate());
                        holder.description.setText(model.getDescription());
                        Picasso.get().load(model.getUserProfileImage()).placeholder(R.drawable.profile).into(holder.user_post_image2);

                       if (model.getPostImage().equals("")){
                           holder.postImage.setVisibility(INVISIBLE);
                           holder.postImage.setMaxHeight(2);

                       }
                        if (!model.getPostImage().equals("")){
                            Picasso.get().load(model.getPostImage()).placeholder(R.drawable.add_post_high).into(holder.postImage);

                       }


                        holder.username.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent mainIntent23 = new Intent(MainActivity.this, ProfileActivity.class);
                                mainIntent23.putExtra("UserKey",visit_user_id);
                                startActivity(mainIntent23);

                            }
                        });

                        holder.user_post_image2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent mainIntent23 = new Intent(MainActivity.this, ProfileActivity.class);
                                mainIntent23.putExtra("UserKey",visit_user_id);
                                startActivity(mainIntent23);

                            }
                        });


                        holder.postImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent setupIntent2 = new Intent(MainActivity.this, ClickPostActivity.class);
                                setupIntent2.putExtra("Post Key",PostKey);
                                startActivity(setupIntent2);

                            }
                        });


                        final String[] PostCat = new String[1];

                        PostsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    PostCat[0] =dataSnapshot.child(PostKey).child("Category").getValue().toString();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                        holder.LikeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                LikeChecker = true;

                                LikesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (LikeChecker== true){

                                            if (dataSnapshot.child(PostKey).hasChild(CurrentUserID)){

                                                LikesRef.child(PostKey).child(CurrentUserID).removeValue();
                                                // PostsRef.child(PostKey).child("Likes").child(CurrentUserID).removeValue();
                                                //  UserRef.child(CurrentUserID).child("LikedPosts").child(PostKey).removeValue();
                                                LikeChecker = false;

                                                UserRef.child(CurrentUserID).child("LikedPosts").child("Category")
                                                        .child(PostCat[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {

                                                            int x = Integer.parseInt(dataSnapshot.getValue().toString()) - 1 ;
                                                            UserRef.child(CurrentUserID).child("LikedPosts").child("Category").child(PostCat[0]).setValue(x);

                                                            totalnolikes=totalnolikes-1;
                                                            UserRef.child(CurrentUserID).child("LikedPosts").child("Total Likes").setValue(totalnolikes);


                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                            else {

                                                LikesRef.child(PostKey).child(CurrentUserID).setValue(true);
                                                //   UserRef.child(CurrentUserID).child("LikedPosts").child(PostKey).child("Category").setValue(PostCat[0]);
                                                // PostsRef.child(PostKey).child("Likes").child(CurrentUserID).setValue(true);
                                                LikeChecker = false;

                                                totalnolikes=totalnolikes+1;
                                                UserRef.child(CurrentUserID).child("LikedPosts").child("Total Likes").setValue(totalnolikes);

                                                UserRef.child(CurrentUserID).child("LikedPosts").child("Category")
                                                        .child(PostCat[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {

                                                            int x = 1+ Integer.parseInt(dataSnapshot.getValue().toString());
                                                            UserRef.child(CurrentUserID).child("LikedPosts").child("Category").child(PostCat[0]).setValue(x);

                                                        }

                                                        else{

                                                            UserRef.child(CurrentUserID).child("LikedPosts").child("Category").child(PostCat[0]).setValue(1);

                                                        }



                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                            }


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                            }
                        });


                        holder.CommentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent setupIntent2 = new Intent(MainActivity.this, CommentActivity.class);
                                setupIntent2.putExtra("PostKey",PostKey);
                                startActivity(setupIntent2);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_posts_layout,parent,false);
                        PostsViewHolder viewHolder=new PostsViewHolder(view);
                        return viewHolder;

                    }
                };
        firebaseRecyclerAdapter.startListening();
        postList.setAdapter(firebaseRecyclerAdapter);

    }




    public static class PostsViewHolder extends RecyclerView.ViewHolder{
        TextView username,date,time,description,NoLikes,NoComments;
        CircleImageView user_post_image2;
        ImageView postImage;
        ImageButton LikeBtn,CommentBtn,editImage;
        int likescount;
        String currentuserid;
        DatabaseReference UsersRef,PostRef2,LikesRef;



        public PostsViewHolder(View itemView) {
            super(itemView);

            LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
            PostRef2 = FirebaseDatabase.getInstance().getReference().child("Posts");
            currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            username=itemView.findViewById(R.id.post_user_name);
            date=itemView.findViewById(R.id.post_date);
            time=itemView.findViewById(R.id.post_time);
            description=itemView.findViewById(R.id.post_description);
            postImage=itemView.findViewById(R.id.post_image);
            user_post_image2=itemView.findViewById(R.id.post_profile_image);
            LikeBtn =itemView.findViewById(R.id.all_posts_like_btn);
            CommentBtn =itemView.findViewById(R.id.all_posts_comment_btn);

            editImage= (ImageButton) itemView.findViewById(R.id.all_posts_edit_btn);
            NoLikes=itemView.findViewById(R.id.likes_no);
            NoComments=itemView.findViewById(R.id.comments_no);



        }





        public void SetLikeButtonStatus(final String postkey){


            LikesRef.child(postkey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(currentuserid)){

                        // likescount = (int) dataSnapshot.child(currentuserid).child("LikedPosts").child(postkey).child("Likes").getChildrenCount();
                        LikeBtn.setImageResource(R.drawable.love1);


                        LikesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(postkey).exists()){
                                    likescount = (int) dataSnapshot.child(postkey).getChildrenCount();
                                    NoLikes.setText(Integer.toString(likescount)+ " Like");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }


                    else {
                        //likescount = (int) dataSnapshot.child(postkey).child("Likes").getChildrenCount();
                        LikeBtn.setImageResource(R.drawable.love12);
                        //  NoLikes.setText(Integer.toString(likescount)+ " Like");
                        LikesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(postkey).exists()){
                                    likescount = (int) dataSnapshot.child(postkey).getChildrenCount();
                                }
                                else{
                                    likescount=0;
                                }
                                NoLikes.setText(Integer.toString(likescount)+ " Like");


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




            PostRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int commentscount;
                    if (dataSnapshot.child(postkey).child("Comments").exists()) {

                        commentscount = (int) dataSnapshot.child(postkey).child("Comments").getChildrenCount();
                        NoComments.setText(Integer.toString(commentscount) + " Comment");
                        CommentBtn.setImageResource(R.drawable.comment4);

                    }

                    else {
                        commentscount = (int) dataSnapshot.child(postkey).child("Comments").getChildrenCount();
                        NoComments.setText(Integer.toString(commentscount) + " Comment");
                        CommentBtn.setImageResource(R.drawable.comment42);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }



    }



    //////////------------------------------------------////////////////


    private void DisplayAllGoing(){

        Query SortPosts = GoinigRef.orderByChild("Counter");

        FirebaseRecyclerOptions<Going> options2 =
                new FirebaseRecyclerOptions.Builder<Going>()
                        .setQuery(SortPosts, Going.class)
                        .build();


        FirebaseRecyclerAdapter<Going,GoingViewHolder> firebaseRecyclerAdapter2 =
                new FirebaseRecyclerAdapter<Going, GoingViewHolder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull final GoingViewHolder questionsViewHolder, int i, @NonNull final Going going) {

                        final String PostK = getRef(i).getKey();
                      final String visit_user_id = going.getUserID();

                        questionsViewHolder.SetGoingButtonStatus(PostK);

                        questionsViewHolder.city1.setText("From: "+going.getFrom());
                        questionsViewHolder.city2.setText("To: "+going.getTo());
                        questionsViewHolder.date1.setText(going.getDate1());
                        questionsViewHolder.date2.setText(going.getDate2());
                        questionsViewHolder.date.setText(going.getDate());
                        questionsViewHolder.time.setText(going.getTime());
                        questionsViewHolder.goingNotes.setText(going.getNotes());

                        UserRef.child(visit_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                questionsViewHolder.uname.setText(dataSnapshot.child("FullName").getValue().toString());
                                Picasso.get().load(dataSnapshot.child("ProfileImage").getValue().toString())
                                        .placeholder(R.drawable.add_post_high)
                                        .into(questionsViewHolder.user_prof);

                        }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                            questionsViewHolder.Goingno.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                        Intent setupIntent2 = new Intent(MainActivity.this, AllGoingActivity.class);
                                        setupIntent2.putExtra("PK", PostK);
                                        startActivity(setupIntent2);

                                }
                            });

                        questionsViewHolder.ImGoing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GoingChecker = true;
                                IsGoing.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (GoingChecker== true){
                                            if (dataSnapshot.child(PostK).hasChild(CurrentUserID)){
                                                IsGoing.child(PostK).child(CurrentUserID).removeValue();
                                                GoingChecker = false;

                                            }

                                            else {
                                                IsGoing.child(PostK).child(CurrentUserID).child("going").setValue("true");
                                                GoingChecker = false;

                                            }
                                            questionsViewHolder.SetGoingButtonStatus(PostK);


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
                    public GoingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view2= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_who_going,parent,false);
                        GoingViewHolder viewHolder2=new GoingViewHolder(view2);
                        return viewHolder2;
                    }
                };


        firebaseRecyclerAdapter2.startListening();
        postList2.setAdapter(firebaseRecyclerAdapter2);

    }



    public static class GoingViewHolder extends RecyclerView.ViewHolder{
        TextView date,time,date1,date2,city1,city2,uname,goingNotes,Goingno;
        CircleImageView user_prof;
        Button ImGoing,cancel;
        DatabaseReference IsGoingRef;
        String currentuserid;



        public GoingViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.going_date);
            time = (TextView) itemView.findViewById(R.id.going_time);
            date1 = (TextView) itemView.findViewById(R.id.date1);
            date2 = (TextView) itemView.findViewById(R.id.date2);
            city1 = (TextView) itemView.findViewById(R.id.city1);
            city2 = (TextView) itemView.findViewById(R.id.city2);
            uname = (TextView) itemView.findViewById(R.id.going_user_name);
            goingNotes = (TextView) itemView.findViewById(R.id.going_notes);
            Goingno = (TextView) itemView.findViewById(R.id.goingno);

            user_prof= (CircleImageView) itemView.findViewById(R.id.going_profile_image);


            ImGoing = (Button) itemView.findViewById(R.id.going_btn);
            cancel = (Button) itemView.findViewById(R.id.going_btn2);

            IsGoingRef= FirebaseDatabase.getInstance().getReference().child("IsGoing");

            currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }


        public void SetGoingButtonStatus(final String pk){
          //  IsGoing

            IsGoingRef.child(pk).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(currentuserid)){

                        ImGoing.setBackgroundResource(R.drawable.button3);
                        ImGoing.setText("Cancel");

                        IsGoingRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(pk).exists()){
                                  int  goingcount = (int) dataSnapshot.child(pk).getChildrenCount();
                                    Goingno.setText(Integer.toString(goingcount)+ " People going");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }


                    else {

                        IsGoingRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int goingcount;
                                if (dataSnapshot.child(pk).exists()){
                                    goingcount = (int) dataSnapshot.child(pk).getChildrenCount();
                                }
                                else{
                                     goingcount=0;
                                }
                                Goingno.setText(Integer.toString(goingcount)+ " People going");
                                ImGoing.setBackgroundResource(R.drawable.buttons);
                                ImGoing.setText("I'm Going");


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

    }





    //////////------------------------------------------////////////////








    private void DisplayAllSchedules() {


        Query SortPosts = ScheduleRef.orderByChild("Counter");

        FirebaseRecyclerOptions<Schedules> options2 =
                new FirebaseRecyclerOptions.Builder<Schedules>()
                        .setQuery(SortPosts, Schedules.class)
                        .build();


        FirebaseRecyclerAdapter<Schedules, SchedulesViewHolder> firebaseRecyclerAdapter2 =
                new FirebaseRecyclerAdapter<Schedules, SchedulesViewHolder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull SchedulesViewHolder schedulesViewHolder, int i, @NonNull Schedules schedules) {

                        final String SchKey = getRef(i).getKey();


                        schedulesViewHolder.cost.setText(schedules.getCost().toUpperCase()+"$ TOTAL COST");
                        schedulesViewHolder.tags.setText(schedules.getTags().toUpperCase());
                        schedulesViewHolder.title.setText(schedules.getTitle().toUpperCase());
                        schedulesViewHolder.location.setText(schedules.getLocation().toUpperCase());
                        schedulesViewHolder.username.setText(schedules.getFullName());
                        schedulesViewHolder.date.setText(schedules.getDate());
                        schedulesViewHolder.time.setText(schedules.getTime());


                        Picasso.get().load(schedules.getProfileImage())
                                .placeholder(R.drawable.add_post_high)
                                .into(schedulesViewHolder.user_post_image3);


                        schedulesViewHolder.seebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent mainIntent22 = new Intent(MainActivity.this, DailyProgramActivity.class);
                                mainIntent22.putExtra("SchKey",SchKey);
                                startActivity(mainIntent22);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SchedulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view2= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_scheduels_layout,parent,false);
                        SchedulesViewHolder viewHolder2=new SchedulesViewHolder(view2);
                        return viewHolder2;
                    }
                };



        firebaseRecyclerAdapter2.startListening();
        postList3.setAdapter(firebaseRecyclerAdapter2);

    }


    public static class SchedulesViewHolder extends RecyclerView.ViewHolder {
        TextView username, date, time, title,location,cost,tags;
        CircleImageView user_post_image3;
        Button seebtn;

        public SchedulesViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.sch_title);
            location = itemView.findViewById(R.id.sch_location);
            cost = itemView.findViewById(R.id.sch_cost);
            tags = itemView.findViewById(R.id.sch_tags);



            username = itemView.findViewById(R.id.sch_user_name);
            date = itemView.findViewById(R.id.sch_date);
            time = itemView.findViewById(R.id.sch_time);
            user_post_image3 = itemView.findViewById(R.id.sch_image);

            seebtn = (Button) itemView.findViewById(R.id.sch_seemore_btn);

        }
    }



    //////////------------------------------------------////////////////



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        JoindRoomsActivity f =new JoindRoomsActivity();

        if(currentUser == null){

            SendUserToLoginActivity();
        }

        else{

            CheckUserExistence();
        }
    }


    private void CheckUserExistence() {

        final String current_user_id = mAuth.getCurrentUser().getUid();

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(current_user_id)){
                    SendUserToSetupActivity();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void SendUserToAllMassagesActivity() {

        Intent setupIntent2 = new Intent(MainActivity.this, AllMasagesActivity.class);
        startActivity(setupIntent2);

    }


    private void SendUserToResetPasswordActivity() {
        Intent setupIntent2 = new Intent(MainActivity.this, ResetPasswordActivity.class);
        startActivity(setupIntent2);
    }

    private void SendUserToProfileActivity() {
        Intent setupIntent2 = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(setupIntent2);
    }


    private void SendUserToSettingsActivity() {
        Intent setupIntent2 = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(setupIntent2);
    }


    private void SendUserToFindFriendsActivity() {
        Intent setupIntent2 = new Intent(MainActivity.this, FindFriendsActivity.class);
        startActivity(setupIntent2);
    }



    private void SendUserToSetupActivity() {
        Intent setupIntent2 = new Intent(MainActivity.this, SetupActivity.class);
        startActivity(setupIntent2);

    }


    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();

    }


    private void SendUserToPostsActivity() {
        Intent setupIntent2 = new Intent(MainActivity.this, PostsActivity.class);
        startActivity(setupIntent2);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void UserMenuSelector(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.nav_near_me:
                SendUserToNearMeActivity();
                break;
            case R.id.nav_cities:
                SendUserToAboutCitiesActivity();
                break;
            case R.id.nav_find_friends:
                SendUserToFindFriendsActivity();
                break;
            case R.id.nav_reqAndoff:
                SendUserToAllReqActivity();
                break;
            case R.id.nav_rooms:
                SendUserToAllRoomsActivity();
                break;
            case R.id.nav_setting:
                SendUserToSettingsActivity();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;

        }

    }

    private void SendUserToAboutCitiesActivity() {
        Intent setupIntent2 = new Intent(MainActivity.this, AboutCitiesActivity.class);
        startActivity(setupIntent2);
    }

    private void SendUserToAllReqActivity() {
        Intent setupIntent2 = new Intent(MainActivity.this, AllReqAndOffersActivity.class);
        startActivity(setupIntent2);
    }

    private void SendUserToNearMeActivity() {
        Intent setupIntent2 = new Intent(MainActivity.this, NearMeActivity.class);
        startActivity(setupIntent2);
    }


    private void BottomItemSelector(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home_icon_btn:
                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                break;


            case R.id.add_icon_btn:
                SendUserToPostsActivity();
                break;

            case R.id.services_icon_btn:
                SendUserToMainServicesActivity();
                break;

            case R.id.profile_icon_btn:
                SendUserToProfileActivity();
                break;


        }
    }

    private void SendUserToMainServicesActivity() {
        Intent setupIntent2 = new Intent(MainActivity.this, MainServicesActivity.class);
        startActivity(setupIntent2);
    }


    private void SendUserToFollowingActivity() {
        Intent mainIntent2 = new Intent(MainActivity.this, FollowingActivity.class);
        mainIntent2.putExtra("ID",CurrentUserID);
        startActivity(mainIntent2);
    }

    private void SendUserToAllRoomsActivity() {

        Intent mainIntent12 = new Intent(MainActivity.this, JoindRoomsActivity.class);
        startActivity(mainIntent12);

    }

}
