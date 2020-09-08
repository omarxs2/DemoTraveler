package com.app.demotraveler;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton SearchBtn;
    private EditText SearchInputText;
    private RecyclerView SearclResultList;
    private DatabaseReference UserRef;
    String CurrentUserID;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        mToolbar=(Toolbar) findViewById(R.id.find_friends_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Find Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearclResultList=(RecyclerView)findViewById(R.id.search_result_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        SearclResultList.setHasFixedSize(true);
       // linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        SearclResultList.setLayoutManager(linearLayoutManager);


        SearchBtn=(ImageButton)findViewById(R.id.find_friends_search_btn);
        SearchInputText=(EditText)findViewById(R.id.search_input);


        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchbox = SearchInputText.getText().toString();
                SearchNewFriends();
            }
        });

      //  SearchNewFriends(" ");


        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            CurrentUserID = mAuth.getCurrentUser().getUid();
        }


    }

    private void SearchNewFriends() {

        /*
        Query query = UserRef.orderByChild("FullName")
                .startAt(searchbox).endAt(searchbox+"\uf8ff");


         */
        FirebaseRecyclerOptions<FindFriends> options =
                new FirebaseRecyclerOptions.Builder<FindFriends>()
                        .setQuery(UserRef, FindFriends.class)
                        .build();


        FirebaseRecyclerAdapter<FindFriends,FindFriendsActivity.UsersViewHolder> adapter =
                new FirebaseRecyclerAdapter<FindFriends,FindFriendsActivity.UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendsActivity.UsersViewHolder holder,
                                            final int position, @NonNull FindFriends model) {

               final String visit_user_id = getRef(position).getKey();


                holder.username.setText("@"+model.getUserName());
                holder.fullname.setText(model.getFullName());
                holder.status.setText(model.getBio());


                Picasso.get().load(model.getProfileImage()).placeholder(R.drawable.profile).into(holder.user_profile_image);



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent mainIntent23 = new Intent(FindFriendsActivity.this, ProfileActivity.class);
                        mainIntent23.putExtra("UserKey",visit_user_id);
                        startActivity(mainIntent23);


                    }
                });


            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_users_display_layout,viewGroup,false);
                FindFriendsActivity.UsersViewHolder viewHolder=new FindFriendsActivity.UsersViewHolder(view);
                return viewHolder;
            }
        };


            adapter.startListening();
            SearclResultList.setAdapter(adapter);



    }







    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        TextView username,fullname,status;
        CircleImageView user_profile_image;
        public UsersViewHolder(View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.find_username);
            fullname=itemView.findViewById(R.id.find_full_name);
            status=itemView.findViewById(R.id.find_biostatus);
            user_profile_image=itemView.findViewById(R.id.search_profile_image);

        }
    }









}
