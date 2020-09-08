package com.app.demotraveler;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClickPostActivity extends AppCompatActivity {
    private ImageView PostImage;
    private TextView PostDes;
    private Button DeleteBtn,EditBtn;
    private String PostKey,current_user_id,databaseUserID,PostImage2,Description;
    private DatabaseReference PostRef;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        PostImage=(ImageView)findViewById(R.id.post_image2);
        PostDes=(TextView)findViewById(R.id.post_des);
        EditBtn=(Button)findViewById(R.id.post_edit_btn);
        DeleteBtn=(Button)findViewById(R.id.post_delete_btn);


        EditBtn.setVisibility(View.INVISIBLE);
        DeleteBtn.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        current_user_id=mAuth.getCurrentUser().getUid();

        PostKey=getIntent().getExtras().get("Post Key").toString();
        PostRef= FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);

        PostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Description = dataSnapshot.child("Description").getValue().toString();
                    PostImage2 = dataSnapshot.child("PostImage").getValue().toString();
                    databaseUserID=dataSnapshot.child("uId").getValue().toString();

                    PostDes.setText(Description);


                    Picasso.get().load(PostImage2).placeholder(R.drawable.add_post_high).into(PostImage);

                    if (current_user_id.equals(databaseUserID)){
                        EditBtn.setVisibility(View.VISIBLE);
                        DeleteBtn.setVisibility(View.VISIBLE);
                    }
                    
                    EditBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditCurrentPost(Description);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCurrentPost();
                Toast.makeText(ClickPostActivity.this,
                        "Post has been deleted..", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void EditCurrentPost(String description) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
        builder.setTitle("Edit Post:");

        final EditText inputField = new EditText(ClickPostActivity.this);
        inputField.setText(description);
        builder.setView(inputField);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                PostRef.child("Description").setValue(inputField.getText().toString());
                Toast.makeText(ClickPostActivity.this, "Post edited successfully", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        Dialog dialog =   builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_bright);
    }


    private void DeleteCurrentPost() {
        PostRef.removeValue();
        SendUserToMainActivity();
    }


    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(ClickPostActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }


}
