package com.app.demotraveler;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class InterestsActivity extends AppCompatActivity {


    private CheckBox c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,
            c11,c12,c13,c14,c15,c16,c17,c18,c19,c20;

    private Button SaveBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);


        c1 = (CheckBox)findViewById(R.id.check_skydiving);
        c2 = (CheckBox)findViewById(R.id.check_mountaineering);
        c3 = (CheckBox)findViewById(R.id.check_rafting);
        c4 = (CheckBox)findViewById(R.id.check_diving);
        c5 = (CheckBox)findViewById(R.id.check_greenlands);
        c6 = (CheckBox)findViewById(R.id.check_oceans);
        c7 = (CheckBox)findViewById(R.id.check_mountains);
        c8 = (CheckBox)findViewById(R.id.check_jungles);
        c9 = (CheckBox)findViewById(R.id.check_istanbul);
        c10 = (CheckBox)findViewById(R.id.check_trabzon);
        c11 = (CheckBox)findViewById(R.id.check_antalya);
        c12 = (CheckBox)findViewById(R.id.check_antep);
        c13 = (CheckBox)findViewById(R.id.check_architecture);
        c14 = (CheckBox)findViewById(R.id.check_parties);
        c15 = (CheckBox)findViewById(R.id.check_animals);
        c16 = (CheckBox)findViewById(R.id.check_food);
        c17 = (CheckBox)findViewById(R.id.check_history);
        c18 = (CheckBox)findViewById(R.id.check_culture);
        c19 = (CheckBox)findViewById(R.id.check_festivals);
        c20 = (CheckBox)findViewById(R.id.check_art);


        SaveBtn = (Button) findViewById(R.id.interests_save_btn);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(current_user_id).child("Interests");


        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDateToDatabase();

            }
        });


    }

    private void SaveDateToDatabase() {

        HashMap userMap = new HashMap();



            if (c1.isChecked() == true){
                userMap.put(c1.getText().toString() ,"true");
            }
            if (c2.isChecked() == true){
                userMap.put(c2.getText().toString() ,"true");
            }
            if (c3.isChecked() == true){
                userMap.put(c3.getText().toString() ,"true");
            }
            if (c4.isChecked() == true){
                userMap.put(c4.getText().toString() ,"true");
            }
            if (c5.isChecked() == true){
                userMap.put(c5.getText().toString() ,"true");
            }
            if (c6.isChecked() == true){
                userMap.put(c6.getText().toString() ,"true");
            }
            if (c7.isChecked() == true){
                userMap.put(c7.getText().toString() ,"true");
            }
            if (c8.isChecked() == true){
                userMap.put(c8.getText().toString() ,"true");
            }
            if (c9.isChecked() == true){
                userMap.put(c9.getText().toString() ,"true");
            }
            if (c10.isChecked() == true){
                userMap.put(c10.getText().toString() ,"true");
            }
            if (c11.isChecked() == true){
                userMap.put(c11.getText().toString() ,"true");
            }
            if (c12.isChecked() == true){
                userMap.put(c12.getText().toString() ,"true");
            }
            if (c13.isChecked() == true){
                userMap.put(c13.getText().toString() ,"true");
            }
            if (c14.isChecked() == true){
                userMap.put(c14.getText().toString() ,"true");
            }
            if (c15.isChecked() == true){
                userMap.put(c15.getText().toString() ,"true");
            }
            if (c16.isChecked() == true){
                userMap.put(c16.getText().toString() ,"true");
            }
            if (c17.isChecked() == true){
                userMap.put(c17.getText().toString() ,"true");
            }
            if (c18.isChecked() == true){
                userMap.put(c18.getText().toString() ,"true");
            }
            if (c19.isChecked() == true){
                userMap.put(c19.getText().toString() ,"true");
            }
            if (c20.isChecked() == true){
                userMap.put(c20.getText().toString() ,"true");
            }





        if (c1.isChecked() == false){
            userMap.put(c1.getText().toString() ,"false");
        }

        if (c2.isChecked() == false){
            userMap.put(c2.getText().toString() ,"false");
        }
        if (c3.isChecked() == false){
            userMap.put(c3.getText().toString() ,"false");
        }
        if (c4.isChecked() == false){
            userMap.put(c4.getText().toString() ,"false");
        }
        if (c5.isChecked() == false){
            userMap.put(c5.getText().toString() ,"false");
        }
        if (c6.isChecked() == false){
            userMap.put(c6.getText().toString() ,"false");
        }
        if (c7.isChecked() == false){
            userMap.put(c7.getText().toString() ,"false");
        }
        if (c8.isChecked() == false){
            userMap.put(c8.getText().toString() ,"false");
        }
        if (c9.isChecked() == false){
            userMap.put(c9.getText().toString() ,"false");
        }
        if (c10.isChecked() == false){
            userMap.put(c10.getText().toString() ,"false");
        }
        if (c11.isChecked() == false){
            userMap.put(c11.getText().toString() ,"false");
        }
        if (c12.isChecked() == false){
            userMap.put(c12.getText().toString() ,"false");
        }
        if (c13.isChecked() == false){
            userMap.put(c13.getText().toString() ,"false");
        }
        if (c14.isChecked() == false){
            userMap.put(c14.getText().toString() ,"false");
        }
        if (c15.isChecked() == false){
            userMap.put(c15.getText().toString() ,"false");
        }
        if (c16.isChecked() == false){
            userMap.put(c16.getText().toString() ,"false");
        }
        if (c17.isChecked() == false){
            userMap.put(c17.getText().toString() ,"false");
        }
        if (c18.isChecked() == false){
            userMap.put(c18.getText().toString() ,"false");
        }
        if (c19.isChecked() == false){
            userMap.put(c19.getText().toString() ,"false");
        }
        if (c20.isChecked() == false){
            userMap.put(c20.getText().toString() ,"false");
        }



        UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isSuccessful()){

                    Toast.makeText(InterestsActivity.this, "Your data was successfully saved..", Toast.LENGTH_SHORT).show();
                    SendUserToMainActivity();

                }
                else{
                    String massage = task.getException().getMessage();
                    Toast.makeText(InterestsActivity.this, massage, Toast.LENGTH_SHORT).show();


                }
            }
        });




    }


    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(InterestsActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }
}
