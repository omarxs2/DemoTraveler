package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class LocalGuidActivity extends AppCompatActivity {


    private Toolbar mToolbat;
    private ImageButton SearchLocalBtn,SeeAllOffers;
    private EditText SearchInput;
    private RecyclerView AllLocals;
    private DatabaseReference LocalRef,UserRef;
    String CurrentUserID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_guid);


            mToolbat= (Toolbar) findViewById(R.id.local_bar);
            setSupportActionBar(mToolbat);
            getSupportActionBar().setTitle("Local Guid");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


            AllLocals=findViewById(R.id.all_guides_list);
            SearchLocalBtn=findViewById(R.id.find_local_search_btn);
            SearchInput=findViewById(R.id.search_local_input);
        SeeAllOffers=findViewById(R.id.go_all_offers);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        AllLocals.setHasFixedSize(true);
        AllLocals.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            CurrentUserID = mAuth.getCurrentUser().getUid();
        }

        LocalRef = FirebaseDatabase.getInstance().getReference().child("LocalGuides");

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        SearchLocals("");


        SearchLocalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchbox = SearchInput.getText().toString();
                SearchLocals(searchbox);
            }
        });



        SeeAllOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToAllReqActivity();
            }
        });



    }





    private void SearchLocals(final String searchbox) {



        Query myQuery = LocalRef.orderByChild("City").startAt(searchbox).endAt(searchbox+"\uf8ff");


        FirebaseRecyclerOptions<Locals> options =
                new FirebaseRecyclerOptions.Builder<Locals>()
                        .setQuery(myQuery, Locals.class)
                        .build();


        FirebaseRecyclerAdapter<Locals, LocalGuidActivity.LocalsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Locals, LocalsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final LocalsViewHolder holder, int i, @NonNull final Locals locals) {

                        final String visit_user_id = getRef(i).getKey();


                            LocalRef.child(visit_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {

                                        holder.city.setText(locals.getCity());

                                        UserRef.child(visit_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {


                                                   final String price = dataSnapshot.child("Business Information").child("Price").getValue().toString();

                                                    String myProImage = dataSnapshot.child("ProfileImage").getValue().toString();
                                                    String myFullName = dataSnapshot.child("FullName").getValue().toString();
                                                    String myStatus = dataSnapshot.child("Bio").getValue().toString();

                                                    String TotalPrice = price + "$" + "/day";


                                                    holder.fullname.setText(myFullName.toUpperCase());
                                                    holder.bio.setText(myStatus);
                                                    holder.price.setText(TotalPrice);

                                                    Picasso.get().load(myProImage).placeholder(R.drawable.profile).into(holder.user_profile_image);


                                                    holder.VisitProf.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent mainIntent23 = new Intent(LocalGuidActivity.this, ProfileActivity.class);
                                                            mainIntent23.putExtra("UserKey", visit_user_id);
                                                            startActivity(mainIntent23);

                                                        }
                                                    });

                                                    holder.BookNow.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent mainIntent23 = new Intent(LocalGuidActivity.this, BookLocalActivity.class);
                                                            mainIntent23.putExtra("UserKey", visit_user_id);
                                                            mainIntent23.putExtra("Price", price);

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

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }





                    @NonNull
                    @Override
                    public LocalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_locals_layout,parent,false);
                        LocalGuidActivity.LocalsViewHolder viewHolder=new LocalGuidActivity.LocalsViewHolder (view);
                        return viewHolder;                    }
                };

        if (adapter != null){
            adapter.startListening();
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        }
        AllLocals.setAdapter(adapter);




    }


    public static class LocalsViewHolder extends RecyclerView.ViewHolder{
        TextView price,fullname,bio,city;
        CircleImageView user_profile_image;
        private Button BookNow,VisitProf;


        public LocalsViewHolder(View itemView) {
            super(itemView);

            price=itemView.findViewById(R.id.local_price);
            fullname=itemView.findViewById(R.id.local_name);
            bio=itemView.findViewById(R.id.local_bio);
            city=itemView.findViewById(R.id.local_city);

            BookNow=(Button)itemView.findViewById(R.id.local_btn1);
            VisitProf=itemView.findViewById(R.id.local_btn2);

            user_profile_image=itemView.findViewById(R.id.local_image);
        }
    }


    private void SendUserToAllReqActivity() {
        Intent mainIntent = new Intent(LocalGuidActivity.this, AllReqAndOffersActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();


    }




}
