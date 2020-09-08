package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnswerActivity extends AppCompatActivity {



    private ImageButton CommentBtn;
    private EditText CommentText;
    private RecyclerView CommentsList;
    String post_key;
    private DatabaseReference UserRef,QuestRef;
    private FirebaseAuth mAuth;
    private String CurrentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);


        CommentBtn = (ImageButton) findViewById(R.id.answer_comment_btn);
        CommentText = (EditText)findViewById(R.id.answer_input);


        CommentsList = (RecyclerView)findViewById(R.id.answers_list);
        CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);


        post_key = getIntent().getExtras().get("PostKey").toString();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        QuestRef = FirebaseDatabase.getInstance().getReference().child("Going").child(post_key).child("Comments");


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            CurrentUserID = mAuth.getCurrentUser().getUid();
        }



        CommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserRef.child(CurrentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            String username = dataSnapshot.child("UserName").getValue().toString();
                            String fullname = dataSnapshot.child("FullName").getValue().toString();
                            String profileimage = dataSnapshot.child("ProfileImage").getValue().toString();


                            ValidateComment(username,fullname,profileimage);

                            CommentText.setText(null);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(QuestRef, Comments.class)
                        .build();

        FirebaseRecyclerAdapter<Comments,AnswerActivity.CommentsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Comments, AnswerActivity.CommentsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AnswerActivity.CommentsViewHolder holder, int position, @NonNull Comments model) {


                        holder.username.setText("@"+model.getUserName());
                        holder.fullname.setText(model.getFullName());
                        holder.date.setText(model.getDate());
                        holder.time.setText(" " +model.getTime());
                        holder.comment.setText(model.getComment());




                        Picasso.get().load(model.getProfileImage()).placeholder(R.drawable.profile).into(holder.user_profile_image);


                    }

                    @NonNull
                    @Override
                    public AnswerActivity.CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_comments_layout,viewGroup,false);
                        AnswerActivity.CommentsViewHolder viewHolder=new AnswerActivity.CommentsViewHolder(view);
                        return viewHolder;            }
                };

        if (adapter != null){
            adapter.startListening();
        }
        CommentsList.setAdapter(adapter);
    }






    public static class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView username,fullname,date,time,comment;
        CircleImageView user_profile_image;

        public CommentsViewHolder(View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.all_comments_username);
            fullname=itemView.findViewById(R.id.all_comments_full_name);
            date=itemView.findViewById(R.id.all_comments_date);
            time=itemView.findViewById(R.id.all_comments_time);
            comment=itemView.findViewById(R.id.all_comments_usercomment);


            user_profile_image=itemView.findViewById(R.id.all_comments_profile_img);
        }






    }





    private void ValidateComment(String username,String fullname,String profileimage) {

        String commenttext = CommentText.getText().toString();

        if (TextUtils.isEmpty(commenttext)){

            Toast.makeText(this, "Please write your comment xxx..", Toast.LENGTH_SHORT).show();

        }
        else{

            Calendar calDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM");
            final String saveCurrentDate = currentDate.format(calDate.getTime());

            Calendar calTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH-mm-ss");
            final String saveCurrentTime = currentTime.format(calTime.getTime());


            final String randomkey = CurrentUserID+saveCurrentDate+saveCurrentTime;

            HashMap commentmap = new HashMap();

            commentmap.put("UId",CurrentUserID);
            commentmap.put("Comment",commenttext);
            commentmap.put("Date",saveCurrentDate);
            commentmap.put("Time",saveCurrentTime);
            commentmap.put("UserName",username);
            commentmap.put("FullName",fullname);
            commentmap.put("ProfileImage",profileimage);


            QuestRef.child(randomkey).updateChildren(commentmap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (task.isSuccessful()){
                                Toast.makeText(AnswerActivity.this, "Your comment is saved.. ", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(AnswerActivity.this, "Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        }



    }






























}
