package com.app.demotraveler;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText useremail,userpassword,userpassword2;
    private Button registerbtn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        useremail = (EditText)findViewById(R.id.register_email);
        userpassword = (EditText)findViewById(R.id.register_passward);
        userpassword2 = (EditText)findViewById(R.id.register_passward2);
        registerbtn = (Button) findViewById(R.id.register_create_account_btn);
        loadingBar = new ProgressDialog(this);





        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            SendUserToMainActivity();
        }
    }


    private void SendVerificationMassage(){
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){

            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Registration Done, Verify your account!", Toast.LENGTH_SHORT).show();
                        SendUserToLoginActivity();
                        mAuth.signOut();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();

                    }
                }
            });

        }



    }

    private void SendUserToLoginActivity() {
        Intent setupIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        setupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void CreateNewAccount() {


        String email = useremail.getText().toString();
        String password = userpassword.getText().toString();
        String password2 = userpassword2.getText().toString();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please write your email..",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write your password..",Toast.LENGTH_SHORT).show();
        }

       else if(TextUtils.isEmpty(password2)){
            Toast.makeText(this,"Please confirm your password..",Toast.LENGTH_SHORT).show();
        }

        else if(!password.equals(password2)){
            Toast.makeText(this,"Your passwords do no match",Toast.LENGTH_SHORT).show();
        }



        else{

            loadingBar.setTitle("Creating new account..");
            loadingBar.setMessage("Please wait and be patient.....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);






            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                       SendVerificationMassage();
                        loadingBar.dismiss();

                    }

                    else{
                        String massage = task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, massage, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }

                }
            });

        }
    }

    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        setupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();

    }
}
