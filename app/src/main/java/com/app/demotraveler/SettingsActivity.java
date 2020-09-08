package com.app.demotraveler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
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

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText UserName,UserFullName,UserStatus,UserCountry,UserGender,UserDB,UserRelationStatus,UserNickname;
    private Button Updatebtn,ForwardBtn,BackBtn,BusSub;
    private CircleImageView UserProfImage;
    private DatabaseReference SetRef,UserRef,LocalRef;
    private FirebaseAuth mAuth;
    private String curentUserID,AccountType,RecieverId;
    final static int Gallery_Pick=1;


    private RadioGroup RadioGroup;
    private RadioButton PrivateBtn,PublicBtn,radioBtn;


    private ProgressDialog loadingBar;
    private StorageReference UserProfileImageRef,TitleImageRef;
    private TabHost tabHost;
    private Spinner spinner;
    private Integer [] images = {R.drawable.image_1,R.drawable.image_2,R.drawable.image_3,
            R.drawable.image_4,R.drawable.image_5,R.drawable.image_6,R.drawable.image_7,
            R.drawable.image_8,R.drawable.image_9};

    private static int i=0;
    private static int xx;

    private ImageSwitcher imageSwitcher;

    private ImageView imageView;

    private TabHost.TabSpec tabSpec;

    private EditText Languages,Citise,WillShow,Activities,Price,offID,offName;
    private TextView per;
    private TextView SeeReviews;
    private String city,agreemet;
    private int pos;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);




        loadingBar = new ProgressDialog(this);




        RadioGroup =(RadioGroup)findViewById(R.id.radio_group);
        PrivateBtn =(RadioButton)findViewById(R.id.private_btn);
        PublicBtn =(RadioButton)findViewById(R.id.public_btn);

        if (getIntent().getExtras() != null) {
            RecieverId = getIntent().getExtras().get("Code").toString();
            agreemet = getIntent().getExtras().get("Agreement").toString();

            if (RecieverId.equals("yes")){
                tabHost = findViewById(android.R.id.tabhost);
                tabHost.setup();
                tabSpec = tabHost.newTabSpec("Screen-1");
                tabSpec.setContent(R.id.activity_tab2);
                tabSpec.setIndicator("Business", null);
                tabHost.addTab(tabSpec);
                tabSpec = tabHost.newTabSpec("Screen-2");
                tabSpec.setContent(R.id.activity_tab1);
                tabSpec.setIndicator("Personal", null);
                tabHost.addTab(tabSpec);



            }
        } else{

            tabHost = findViewById(android.R.id.tabhost);
            tabHost.setup();
            tabSpec = tabHost.newTabSpec("Screen-1");
            tabSpec.setContent(R.id.activity_tab1);
            tabSpec.setIndicator("Personal", null);
            tabHost.addTab(tabSpec);
            tabSpec = tabHost.newTabSpec("Screen-2");
            tabSpec.setContent(R.id.activity_tab2);
            tabSpec.setIndicator("Business", null);
            tabHost.addTab(tabSpec);



        }




        int CheckedBtn = RadioGroup.getCheckedRadioButtonId();
        radioBtn = findViewById(CheckedBtn);


        RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
                if(checkedId == R.id.private_btn) {

                    AccountType="Private";
                } else if(checkedId == R.id.public_btn) {

                    AccountType="Public";

                }


            }
        });

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");




        imageSwitcher = (ImageSwitcher) findViewById(R.id.image_switcher);
        ForwardBtn= (Button) findViewById(R.id.forward_btn);
        BackBtn= (Button) findViewById(R.id.back_btn);

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



        spinner = findViewById(R.id.cities_spinner);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.CitiesList,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);


        UserName = (EditText) findViewById(R.id.settings_username);
        UserFullName = (EditText) findViewById(R.id.settings_full_name);
        UserStatus = (EditText) findViewById(R.id.settings_status);
        UserRelationStatus = (EditText) findViewById(R.id.settings_relationship);
        UserCountry = (EditText) findViewById(R.id.settings_country);
        UserGender = (EditText) findViewById(R.id.settings_gender);
        UserDB = (EditText) findViewById(R.id.settings_dob);
        Updatebtn = (Button) findViewById(R.id.update_account_setting_btn);
        UserProfImage = (CircleImageView) findViewById(R.id.settings_profile_image);
        UserNickname=(EditText) findViewById(R.id.settings_nickname);
        LocalRef = FirebaseDatabase.getInstance().getReference().child("LocalGuides");






        Languages = (EditText) findViewById(R.id.bus_lang_you);
        Citise = (EditText) findViewById(R.id.bus_cities_you);
        WillShow = (EditText) findViewById(R.id.bus_show_you);
        Activities = (EditText) findViewById(R.id.bus_activities_you);
        Price = (EditText) findViewById(R.id.bus_price_you);
        BusSub = (Button) findViewById(R.id.bus_sub);

        offID = (EditText) findViewById(R.id.officialID);
        offName = (EditText) findViewById(R.id.Fullgreatname);


        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("ProfileImage");
        mAuth=FirebaseAuth.getInstance();
        curentUserID=mAuth.getCurrentUser().getUid();
        SetRef= FirebaseDatabase.getInstance().getReference().child("Users").child(curentUserID);

        TitleImageRef = FirebaseStorage.getInstance().getReference().child("TitleImages");




        SetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String xmeTitleImage = dataSnapshot.child("TitleImage").getValue().toString();
                    String myProImage = dataSnapshot.child("ProfileImage").getValue().toString();
                    String myUserName = dataSnapshot.child("UserName").getValue().toString();
                    String myFullName = dataSnapshot.child("FullName").getValue().toString();
                    String myCountry = dataSnapshot.child("Country").getValue().toString();
                    String myStatus = dataSnapshot.child("Bio").getValue().toString();
                    String myGender = dataSnapshot.child("Gender").getValue().toString();
                    String meDOB = dataSnapshot.child("DateOfBirth").getValue().toString();
                    String meRelationship = dataSnapshot.child("Relationship").getValue().toString();
                    String myNickname = dataSnapshot.child("Nickname").getValue().toString();
                    String accountType = dataSnapshot.child("AccountType").getValue().toString();


                    UserFullName.setText(myFullName);
                    UserCountry.setText(myCountry);
                    UserName.setText(myUserName);
                    UserNickname.setText(myNickname);
                    UserStatus.setText(myStatus);
                    UserGender.setText(myGender);
                    UserDB.setText(meDOB);
                    UserRelationStatus.setText(meRelationship);


                    Picasso.get().load(myProImage).placeholder(R.drawable.profile).into(UserProfImage);


                    if (accountType.equals("Private")){

                        PrivateBtn.setChecked(true);
                    } else {
                        PublicBtn.setChecked(true);

                    }



                    if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_1.png?alt=media&token=c648401a-b0cf-4b80-a328-4173440d28eb")){
                        imageSwitcher.setImageResource(R.drawable.image_1);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_2.png?alt=media&token=c5d467d9-845e-4477-bb83-b05157c6af52")){
                        imageSwitcher.setImageResource(R.drawable.image_2);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_3.png?alt=media&token=e553bd51-8350-4749-84ce-1a20a73329b9")){
                        imageSwitcher.setImageResource(R.drawable.image_3);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_4.png?alt=media&token=4e792030-a7e7-4495-a25b-5a3ae5bb6a30")){
                        imageSwitcher.setImageResource(R.drawable.image_4);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_5.png?alt=media&token=efb57363-798b-4a7e-a56f-031bed0296f8")){
                        imageSwitcher.setImageResource(R.drawable.image_5);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_6.png?alt=media&token=f1beb07c-3280-4377-8fde-53cf42c5936d")){
                        imageSwitcher.setImageResource(R.drawable.image_6);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_7.png?alt=media&token=78c3edb5-bdee-4061-90b1-48190f9fb739")){
                        imageSwitcher.setImageResource(R.drawable.image_7);
                    }
                    else if (xmeTitleImage.equals("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_8.png?alt=media&token=760de7c3-4012-44dc-8ccb-8bdcae13eb14")){
                        imageSwitcher.setImageResource(R.drawable.image_8);
                    }
                    else{
                        imageSwitcher.setImageResource(R.drawable.image_9);
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xx=i;
                if (xx==0){
                    SetRef.child("TitleImage")
                            .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_1.png?alt=media&token=c648401a-b0cf-4b80-a328-4173440d28eb");
                }
                else if (xx==1){
                    SetRef.child("TitleImage")
                            .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_2.png?alt=media&token=c5d467d9-845e-4477-bb83-b05157c6af52");
                }
                else if (xx==2){
                    SetRef.child("TitleImage")
                            .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_3.png?alt=media&token=e553bd51-8350-4749-84ce-1a20a73329b9");
                }
                else if (xx==3){
                    SetRef.child("TitleImage")
                            .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_4.png?alt=media&token=4e792030-a7e7-4495-a25b-5a3ae5bb6a30");
                }
                else if (xx==4){
                    SetRef.child("TitleImage")
                            .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_5.png?alt=media&token=efb57363-798b-4a7e-a56f-031bed0296f8");
                }
                else if (xx==5){
                    SetRef.child("TitleImage")
                            .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_6.png?alt=media&token=f1beb07c-3280-4377-8fde-53cf42c5936d");
                }
                else if (xx==6){
                    SetRef.child("TitleImage")
                            .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_7.png?alt=media&token=78c3edb5-bdee-4061-90b1-48190f9fb739");
                }
                else if (xx==7){
                    SetRef.child("TitleImage")
                            .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_8.png?alt=media&token=760de7c3-4012-44dc-8ccb-8bdcae13eb14");
                }
                else{
                    SetRef.child("TitleImage")
                            .setValue("https://firebasestorage.googleapis.com/v0/b/demotraveler-a506c.appspot.com/o/TitleImages%2Fimage_9.png?alt=media&token=1ddfefdd-8dc6-4324-8b3e-79c0ca9c9a28");
                }


                UserRef.child(curentUserID).child("AccountType").setValue(AccountType)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    Toast.makeText(SettingsActivity.this, "Account type is up to date", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                ValidateProfileInfo();
            }
        });

        UserProfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);

                Toast.makeText(SettingsActivity.this, "Select a Photo... ", Toast.LENGTH_SHORT).show();

            }
        });





        UserRef.child(curentUserID).child("Business Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){


                    String cities = dataSnapshot.child("Cities").getValue().toString();
                    String activities = dataSnapshot.child("Activities").getValue().toString();
                    String languages = dataSnapshot.child("Languages").getValue().toString();
                    String show = dataSnapshot.child("Will Show").getValue().toString();
                    String price = dataSnapshot.child("Price").getValue().toString();
                    String officialid = dataSnapshot.child("OfficialID").getValue().toString();
                    String officialname = dataSnapshot.child("OfficialName").getValue().toString();

                    int pos = Integer.parseInt(dataSnapshot.child("Position").getValue().toString());

                    Citise.setText(cities);
                    Activities.setText(activities);
                    Languages.setText(languages);
                    WillShow.setText(show);
                    Price.setText(price);
                    offID.setText(officialid);
                    offName.setText(officialname);

                    spinner.setSelection(pos);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        BusSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position= pos;
                String cities = city;
                String activities = Activities.getText().toString();
                String languages = Languages.getText().toString();
                String show = WillShow.getText().toString();
                String price = Price.getText().toString();
                String name = offName.getText().toString();
                String id = offID.getText().toString();


                if (TextUtils.isEmpty(cities)){
                    Toast.makeText(SettingsActivity.this, "Fill the empty", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(activities)){
                    Toast.makeText(SettingsActivity.this, "Fill the empty", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(languages)){
                    Toast.makeText(SettingsActivity.this, "Fill the empty", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(show)){
                    Toast.makeText(SettingsActivity.this, "Fill the empty", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(price)){
                    Toast.makeText(SettingsActivity.this, "Fill the empty", Toast.LENGTH_SHORT).show();
                }

                else{
                    SaveDataToDB(cities,activities,languages,show,price,position,name,id);

                }



            }
        });

    }



    private void SaveDataToDB(final String cities, String activities, String languages,
                              String show, String price, int position,String name,String id) {

        HashMap hashMap = new HashMap();

        hashMap.put("Cities",cities);
        hashMap.put("Activities",activities);
        hashMap.put("Languages",languages);
        hashMap.put("Will Show",show);
        hashMap.put("Price",price);
        hashMap.put("Position",position);
        hashMap.put("Agreement",agreemet);
        hashMap.put("OfficialID",id);
        hashMap.put("OfficialName",name);


        UserRef.child(curentUserID).child("Business Information").updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()){
                    LocalRef.child(curentUserID).child("City").setValue(cities);
                    Toast.makeText(SettingsActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    SendUserToProfileActivity();
                }

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

        final StorageReference filepath = UserProfileImageRef.child(curentUserID + ".jpg");



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

                    final String downloadUrl = downUri.toString();
                    SetRef.child("ProfileImage").setValue(downloadUrl)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent selfIntent = new Intent(SettingsActivity.this, SettingsActivity.class);
                                       startActivity(selfIntent);
                                        Toast.makeText(SettingsActivity.this, "Profile Image Updated...", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SettingsActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }


            }
        });


    }


    private void ValidateProfileInfo() {

        String username = UserName.getText().toString();
        String fullname = UserFullName.getText().toString();
        String dob = UserDB.getText().toString();
        String country = UserCountry.getText().toString();
        String status = UserStatus.getText().toString();
        String gender = UserGender.getText().toString();
        String relstinship = UserRelationStatus.getText().toString();
        String nickname = UserNickname.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please fill your username..", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "Please fill your fullname..", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(dob)){
            Toast.makeText(this, "Please fill your dob..", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(country)){
            Toast.makeText(this, "Please fill your country..", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(status)){
            Toast.makeText(this, "Please fill your status..", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(gender)){
            Toast.makeText(this, "Please fill your gender..", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(relstinship)){
            Toast.makeText(this, "Please fill your relationship..", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(nickname)){
            Toast.makeText(this, "Please fill your nickname..", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Updating...");
            loadingBar.setMessage("Please wait and be patient.....");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            UpdateAccountInfo(username, fullname, dob ,country,status ,gender ,relstinship,nickname);
        }


    }


    private void UpdateAccountInfo(String username, String fullname, String dob, String country, String status, String gender, String relstinship,String nickname) {



        HashMap hashMap = new HashMap();

        hashMap.put("UserName",username);
        hashMap.put("FullName",fullname);
        hashMap.put("Country",country);
        hashMap.put("Bio",status);
        hashMap.put("Gender",gender);
        hashMap.put("Relationship",relstinship);
        hashMap.put("DateOfBirth",dob);
        hashMap.put("Nickname",nickname);

        SetRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    loadingBar.dismiss();
                    Toast.makeText(SettingsActivity.this, "Information has been updated", Toast.LENGTH_SHORT).show();
                    SendUserToProfileActivity();
                }
                else {
                    Toast.makeText(SettingsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }


    private void SendUserToProfileActivity() {

        Intent mainIntent = new Intent(SettingsActivity.this, ProfileActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getItemAtPosition(position).equals("City")){

            Toast.makeText(this, "Please choose a city", Toast.LENGTH_SHORT).show();
        }
        else{

            city = parent.getItemAtPosition(position).toString();
            pos=position;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
