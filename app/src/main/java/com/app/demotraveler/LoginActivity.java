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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private Button loginbtn,NewAccountLink,PhoneSignin;
    private EditText UserEmail,UserPassward;
    private TextView ForgetPass;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private ImageView facebookbtn;
    private Boolean EmailChecker;

    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        NewAccountLink =  findViewById(R.id.emailsigninbtn);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassward = (EditText) findViewById(R.id.login_passward);
        loginbtn = (Button) findViewById(R.id.login_button);
        PhoneSignin  =  findViewById(R.id.phonesigninbtn);



        NewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });


        PhoneSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPhoneActivity();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowingUserToLogin();
            }
        });


        ForgetPass= (TextView) findViewById(R.id.forget_pass);

        ForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

    }

    private void SendUserToPhoneActivity() {
        Intent registrationIntent = new Intent(LoginActivity.this , PhoneActivity.class);
        startActivity(registrationIntent);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            SendUserToMainrActivity();
        }
    }



    private void AllowingUserToLogin() {

        String email = UserEmail.getText().toString();
        String password = UserPassward.getText().toString();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please write your email..",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write your password..",Toast.LENGTH_SHORT).show();
        }

        else {

            loadingBar.setTitle("Wait..");
            loadingBar.setMessage("Please wait and be patient.....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        VerifyingAccount();

                        loadingBar.dismiss();

                    }

                    else {
                        String massage = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, massage, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                }
            });

        }


    }



    private void VerifyingAccount(){

        FirebaseUser user = mAuth.getCurrentUser();
        EmailChecker = user.isEmailVerified();

        if (EmailChecker){
            SendUserToMainrActivity();
        }
        else{
            Toast.makeText(this, "Please Verify your account first", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }

    }

    private void SendUserToMainrActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }


    private void SendUserToRegisterActivity() {

            Intent registrationIntent = new Intent(LoginActivity.this , RegisterActivity.class);
            startActivity(registrationIntent);

    }











}
