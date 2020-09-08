package com.app.demotraveler;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private Toolbar mToolbat;
    private ImageButton AddPhotoBtn, SendBtn;
    private EditText inputMessage;
    private RecyclerView MessagesList;
    String ReceiverID,FullName;
    private CircleImageView VisitedImage;
    private TextView VisitedUserName;
    private DatabaseReference UserRef,RootRef;
    private FirebaseAuth mAuth;
    String CurrentUserID,saveCurrentDate,saveCurrentTime;
    private final List<Massages> massagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MassagesAdapter massagesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserID = mAuth.getCurrentUser().getUid();


        RootRef = FirebaseDatabase.getInstance().getReference();
        ReceiverID = getIntent().getExtras().get("UserKey").toString();
        FullName = getIntent().getExtras().get("FullName").toString();

        InitializeData();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");



        UserRef.child(ReceiverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    final String username = dataSnapshot.child("UserName").getValue().toString();
                    final String image = dataSnapshot.child("ProfileImage").getValue().toString();


                    VisitedUserName.setText("@"+username.toLowerCase());




                    Picasso.get().load(image).placeholder(R.drawable.profile).into(VisitedImage);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendMassage();

            }
        });




        FetchMassages();




    }

    private void FetchMassages() {

        RootRef.child("Massages").child(CurrentUserID).child(ReceiverID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){

                    Massages massages = dataSnapshot.getValue(Massages.class);
                    massagesList.add(massages);
                    massagesAdapter.notifyDataSetChanged();

                    MessagesList.smoothScrollToPosition(MessagesList.getAdapter().getItemCount());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    private void SendMassage() {

        String massageText = inputMessage.getText().toString();

        if (TextUtils.isEmpty(massageText)){

            Toast.makeText(this, "Please write a massage first", Toast.LENGTH_SHORT).show();
        }
        else{

            String massageSenderRef= "Massages/"+CurrentUserID+"/"+ReceiverID;
            String massageRecieverRef= "Massages/"+ReceiverID+"/"+CurrentUserID;


            DatabaseReference userMassagesRef = RootRef
                    .child("Massages").child(massageSenderRef).child(massageRecieverRef).push();

            String MassagePushID=userMassagesRef.getKey();


            Calendar calDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(calDate.getTime());

            Calendar calTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH-mm aa");
            saveCurrentTime = currentTime.format(calTime.getTime());

            Map massageBody = new HashMap();
            massageBody.put("Massage",massageText);
            massageBody.put("Time",saveCurrentTime);
            massageBody.put("Date",saveCurrentDate);
            massageBody.put("Sender",CurrentUserID);
            massageBody.put("Type","text");

            Map massageDeatails = new HashMap();
            massageDeatails.put(massageSenderRef + "/" +MassagePushID,massageBody );
            massageDeatails.put(massageRecieverRef + "/" +MassagePushID,massageBody );


            RootRef.updateChildren(massageDeatails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChatActivity.this, "Massage Sent!", Toast.LENGTH_SHORT).show();
                        inputMessage.setText("");
                    }
                    else{
                        String mass = task.getException().toString();
                        Toast.makeText(ChatActivity.this, mass, Toast.LENGTH_SHORT).show();
                        inputMessage.setText("");

                    }
                }
            });


        }



    }


    private void InitializeData() {


        mToolbat=  findViewById(R.id.chat_toolbar);
        setSupportActionBar(mToolbat);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionbarview =  inflater.inflate(R.layout.chat_custom_bar,null);
        actionBar.setCustomView(actionbarview);






        AddPhotoBtn = (ImageButton)findViewById(R.id.chat_add_image_btn);
        SendBtn = (ImageButton)findViewById(R.id.chat_message_send_btn);
        inputMessage = (EditText) findViewById(R.id.chat_message_input);
        VisitedUserName= (TextView) findViewById(R.id.custom_chat_username);
        VisitedImage= (CircleImageView) findViewById(R.id.custom_chat_image);



        massagesAdapter = new MassagesAdapter(massagesList);
        MessagesList = (RecyclerView)findViewById(R.id.chat_rec_view);
        linearLayoutManager = new LinearLayoutManager(this);
        MessagesList.setHasFixedSize(true);
        MessagesList.setLayoutManager(linearLayoutManager);
        MessagesList.setAdapter(massagesAdapter);
    }
}
