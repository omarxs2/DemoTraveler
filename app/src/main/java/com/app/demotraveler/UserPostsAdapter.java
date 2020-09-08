package com.app.demotraveler;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserPostsAdapter extends RecyclerView.Adapter <UserPostsAdapter.MyHolder>{

    private DatabaseReference PostsRef;
    private int images[];
    private String names[];
    private Context context;
    private FirebaseAuth mAuth;
    String CurrentUserID;


    public UserPostsAdapter(int[] images, String[] names, Context context) {
        this.images = images;
        this.names = names;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            CurrentUserID = mAuth.getCurrentUser().getUid();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
       // PostsRef.child(CurrentUserID).child("PostImage").get
        //holder.img.setImageResource();

    }

    @Override
    public int getItemCount() {
        return names.length;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txt;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.imageView);
            txt = (TextView) itemView.findViewById(R.id.textView);

        }
    }
}
