package com.app.demotraveler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NormalPostsActivity extends AppCompatActivity {


    private Toolbar mToolbat;
    private ImageButton selectimage;
    private Button publishbtn;
    private EditText postDescription;
    private static final int Gallery_Pick=1;
    private Uri ImageUri;
    private String Description,saveCurrentDate,saveCurrentTime,randomName,current_user_id;
    private StorageReference postImagesRef;
    private DatabaseReference UserRef,PostsRef,UserRef2;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private String category,city;
    private long countPosts=0;
    private String downloadUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_posts);


        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.posttype,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new class1());

        /////////////////////////////

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 =  ArrayAdapter.createFromResource(this,R.array.CitiesList,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new class2());


        postImagesRef = FirebaseStorage.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        UserRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
        loadingBar = new ProgressDialog(this);


        mToolbat= (Toolbar) findViewById(R.id.posts_page_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("New Post..");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectimage =findViewById(R.id.normal_posts_image);
        publishbtn =findViewById(R.id.normal_posts_publish_btn);
        postDescription =findViewById(R.id.normal_posts_text);

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        publishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();

            }
        });




    }






    private void ValidatePostInfo() {

        Description = postDescription.getText().toString();


        if (ImageUri == null){
            Toast.makeText(this, "Please Select an Image first.. ", Toast.LENGTH_SHORT).show();

        }

        else if (TextUtils.isEmpty(Description)){
            Toast.makeText(this, "Please Describe your Image first.. ", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Adding New Post...");
            loadingBar.setMessage("Please wait and be patient.....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            StoringImageToFirebaseStorage();


        }
    }


    private void StoringImageToFirebaseStorage() {


        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calDate.getTime());

        Calendar calTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH-mm-ss");
        saveCurrentTime = currentTime.format(calTime.getTime());

        randomName=saveCurrentTime+saveCurrentDate;

        final StorageReference FilePath = postImagesRef.child("PostImages").child(ImageUri.getLastPathSegment()+randomName+".jpg");



        FilePath.putFile(ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return FilePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downUri = task.getResult();

                    Toast.makeText(NormalPostsActivity.this, "Image Uploaded successfully..", Toast.LENGTH_SHORT).show();

                    downloadUrl = downUri.toString();

                    SavingPostsInfoToDB();

                }
                else {
                    String message = task.getException().getMessage();
                    Toast.makeText(NormalPostsActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();

                }


            }
        });



    }


    private void SavingPostsInfoToDB() {

        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calDate.getTime());

        Calendar calTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH-mm-ss");
        saveCurrentTime = currentTime.format(calTime.getTime());

        randomName=saveCurrentTime+saveCurrentDate;


        PostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    countPosts = dataSnapshot.getChildrenCount();
                }
                else{
                    countPosts=0;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        UserRef.child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){


                    String userFullName = dataSnapshot.child("FullName").getValue().toString();
                    String userProfileImage = dataSnapshot.child("ProfileImage").getValue().toString();

                    HashMap PostsMap = new HashMap();


                    PostsMap.put("PostImage",downloadUrl);
                    PostsMap.put("uId",current_user_id);
                    PostsMap.put("Date",saveCurrentDate);
                    PostsMap.put("Time",saveCurrentTime);
                    PostsMap.put("Description",Description);
                    PostsMap.put("UserProfileImage",userProfileImage);
                    PostsMap.put("FullName",userFullName);
                    PostsMap.put("Category",category);
                    PostsMap.put("Counter",countPosts);
                    PostsMap.put("City",city);




                    PostsRef.child(current_user_id+randomName).updateChildren(PostsMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                SendUserToMainActivity();
                                Toast.makeText(NormalPostsActivity.this, "Post Saved", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();


                            }
                            else {
                                Toast.makeText(NormalPostsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }

                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(NormalPostsActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_Pick);

        Toast.makeText(NormalPostsActivity.this, "Select a Photo... ", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data !=null){
            ImageUri=data.getData();
            selectimage.setImageURI(ImageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            SendUserToPostsActivity();
        }
        return super.onOptionsItemSelected(item);
    }


    private void SendUserToPostsActivity() {
        Intent mainIntent = new Intent(NormalPostsActivity.this, PostsActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();


    }






    private class class1 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (parent.getItemAtPosition(position).equals("Choose Category")){

                Toast.makeText(NormalPostsActivity.this, "Select first", Toast.LENGTH_SHORT).show();

            }
            else{

                category = parent.getItemAtPosition(position).toString();

            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }








    private class class2 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getItemAtPosition(i).equals("City")){

                Toast.makeText(NormalPostsActivity.this, "Please Chose a City ", Toast.LENGTH_SHORT).show();            }
            else{

                city = adapterView.getItemAtPosition(i).toString();

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


}
