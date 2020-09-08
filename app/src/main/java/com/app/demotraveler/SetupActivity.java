package com.app.demotraveler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {


    private EditText Username,Fullname,Countryname,DOF,Gender,Relationship,Nickname,Bio;
    private Button savebtn,ForwardBtn,BackBtn;
    private CircleImageView Userprofileimage;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private String current_user_id;
    private ProgressDialog loadingBar;
    final static int Gallery_Pick=1;
    private StorageReference UserProfileImageRef;


    private Integer [] images = {R.drawable.image_1,R.drawable.image_2,R.drawable.image_3,
            R.drawable.image_4,R.drawable.image_5,R.drawable.image_6,R.drawable.image_7,
            R.drawable.image_8,R.drawable.image_9};

    private ImageSwitcher imageSwitcher;
    private ImageView imageView;

    private static int i=0;
    private static int xx;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        ForwardBtn = (Button) findViewById(R.id.setup_forward_btn);
        BackBtn= (Button) findViewById(R.id.setup_back_btn);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.setup_image_switcher);


        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new
                        ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT ));

                return imageView;
            }
        });


        imageSwitcher.setImageResource(images[0]);


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i > 0){
                    i--;
                    imageSwitcher.setImageResource(images[i]);
                }

            }
        });

        ForwardBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (i < images.length-1){
                    i++;
                    imageSwitcher.setImageResource(images[i]);
                }
            }
        });



        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
        loadingBar = new ProgressDialog(this);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("ProfileImage");

        Username = (EditText)findViewById(R.id.setup_username);
        Fullname = (EditText)findViewById(R.id.setup_full_name);
        Countryname = (EditText)findViewById(R.id.setup_country);

        DOF = (EditText)findViewById(R.id.setup_dob);
        Gender = (EditText)findViewById(R.id.setup_gender);
        Relationship = (EditText)findViewById(R.id.setup_relationship);
        Nickname = (EditText)findViewById(R.id.setup_nickname);
        Bio = (EditText)findViewById(R.id.setup_status);

        Userprofileimage = (CircleImageView) findViewById(R.id.setup_profile_image);

        savebtn = (Button) findViewById(R.id.setup_save_button);



        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountSetupInfo();
            }
        });


        Userprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);

                Toast.makeText(SetupActivity.this, "Select a Photo... ", Toast.LENGTH_SHORT).show();

            }
        });

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.hasChild("ProfileImage")) {

                        String Image = dataSnapshot.child("ProfileImage").getValue().toString();


                        Picasso.get().load(Image).placeholder(R.drawable.profile).into(Userprofileimage);


                    }
                    else
                    {
                        Toast.makeText(SetupActivity.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        loadingBar.setTitle("Saving...");
        loadingBar.setMessage("Please wait and be patient.....");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        final Uri ImageUri = data.getData();

        final StorageReference filepath = UserProfileImageRef.child(current_user_id + ".jpg");



        filepath.putFile(ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downUri = task.getResult();
                    Toast.makeText(SetupActivity.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();

                    final String downloadUrl = downUri.toString();
                    UserRef.child("ProfileImage").setValue(downloadUrl)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent selfIntent = new Intent(SetupActivity.this, SetupActivity.class);
                                        startActivity(selfIntent);

                                        Toast.makeText(SetupActivity.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SetupActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }


            }
        });


    }


    private void SaveAccountSetupInfo() {

        String username = Username.getText().toString();
        String fullname = Fullname.getText().toString();
        String country = Countryname.getText().toString();
        String birthdate = DOF.getText().toString();
        String nickname = Nickname.getText().toString();
        String relation = Relationship.getText().toString();
        String gender = Gender.getText().toString();
        String bio = Bio.getText().toString();
        xx=i;

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this," Please write your username..",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this,"Please write your name..",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(country)){
            Toast.makeText(this,"Please write your country..",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(birthdate)){
            Toast.makeText(this,"Please write your birthday..",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(nickname)){
            Toast.makeText(this,"Please write your nickname..",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(relation)){
            Toast.makeText(this,"Please write your relation..",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(gender)){
            Toast.makeText(this,"Please write your gender..",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(bio)){
            Toast.makeText(this,"Please write your bio..",Toast.LENGTH_SHORT).show();
        }



        else{

            loadingBar.setTitle("Saving...");
            loadingBar.setMessage("Please wait and be patient.....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();

            userMap.put("UserName",username);
            userMap.put("FullName",fullname);
            userMap.put("Country",country);
            userMap.put("Bio",bio);
            userMap.put("DateOfBirth",birthdate);
            userMap.put("Gender",gender);
            userMap.put("Relationship",relation);
            userMap.put("Nickname",nickname);
            userMap.put("TitleImage","");
            userMap.put("AccountType","Public");





            UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful()){
                        SendUserToInterestsActivity();
                        Toast.makeText(SetupActivity.this, "Your data was successfully saved..", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                    else{
                        String massage = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, massage, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                }
            });


            if (xx==0){
                UserRef.child("TitleImage")
                        .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_1.png?alt=media&token=c648401a-b0cf-4b80-a328-4173440d28eb");
            }
            else if (xx==1){
                UserRef.child("TitleImage")
                        .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_2.png?alt=media&token=c5d467d9-845e-4477-bb83-b05157c6af52");
            }
            else if (xx==2){
                UserRef.child("TitleImage")
                        .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_3.png?alt=media&token=e553bd51-8350-4749-84ce-1a20a73329b9");
            }
            else if (xx==3){
                UserRef.child("TitleImage")
                        .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_4.png?alt=media&token=4e792030-a7e7-4495-a25b-5a3ae5bb6a30");
            }
            else if (xx==4){
                UserRef.child("TitleImage")
                        .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_5.png?alt=media&token=efb57363-798b-4a7e-a56f-031bed0296f8");
            }
            else if (xx==5){
                UserRef.child("TitleImage")
                        .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_6.png?alt=media&token=f1beb07c-3280-4377-8fde-53cf42c5936d");
            }
            else if (xx==6){
                UserRef.child("TitleImage")
                        .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_7.png?alt=media&token=78c3edb5-bdee-4061-90b1-48190f9fb739");
            }
            else if (xx==7){
                UserRef.child("TitleImage")
                        .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_8.png?alt=media&token=760de7c3-4012-44dc-8ccb-8bdcae13eb14");
            }
            else if (xx==8){
                UserRef.child("TitleImage")
                        .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_9.png?alt=media&token=1ddfefdd-8dc6-4324-8b3e-79c0ca9c9a28");
            }



        }



    }

    private void SendUserToInterestsActivity() {

        Intent mainIntent = new Intent(SetupActivity.this, InterestsActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }
}
