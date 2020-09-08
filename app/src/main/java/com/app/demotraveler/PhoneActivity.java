package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {
    private Toolbar mToolbat;
    private EditText PhoneNumber,VerificationMassage;
    private Button SendMassage,Verify;
    private FirebaseAuth mAuth;
    private TextView TextMassage,TextMassage2;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);


        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);


        mToolbat = (Toolbar) findViewById(R.id.phone_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Sign in with phone");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        VerificationMassage = findViewById(R.id.verification_massage);
        VerificationMassage.setVisibility(View.INVISIBLE);


        Verify = findViewById(R.id.verify_acc);

        PhoneNumber = findViewById(R.id.phone_number);
        SendMassage = findViewById(R.id.send_massage);
        TextMassage = findViewById(R.id.text_massage);
        TextMassage2 = findViewById(R.id.text_massage2);



        SendMassage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNo = PhoneNumber.getText().toString();


                if (TextUtils.isEmpty(phoneNo)) {
                    Toast.makeText(PhoneActivity.this, "Please Write your phone number", Toast.LENGTH_SHORT).show();
                } else {



                    PhoneAuthProvider.getInstance().verifyPhoneNumber(

                            phoneNo,
                            60,
                            TimeUnit.SECONDS,
                            PhoneActivity.this,
                            mCallbacks);

                }
            }
        });










        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneNumber.setVisibility(View.INVISIBLE);
                VerificationMassage.setVisibility(View.VISIBLE);
                TextMassage.setVisibility(View.INVISIBLE);
                TextMassage2.setVisibility(View.VISIBLE);
                SendMassage.setVisibility(View.INVISIBLE);
                Verify.setVisibility(View.VISIBLE);

                String Vcode = VerificationMassage.getText().toString();

                if (TextUtils.isEmpty(Vcode)){

                    Toast.makeText(PhoneActivity.this, "write the code first", Toast.LENGTH_SHORT).show();

                }
                else{


                    loadingBar.setTitle("Code Verification");
                    loadingBar.setMessage("Please wait and be patient.....");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, Vcode);
                    signInWithPhoneAuthCredential(credential);


                }

            }
        });











        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                loadingBar.dismiss();


                PhoneNumber.setVisibility(View.VISIBLE);
                VerificationMassage.setVisibility(View.INVISIBLE);
                TextMassage.setVisibility(View.VISIBLE);
                TextMassage2.setVisibility(View.INVISIBLE);
                SendMassage.setVisibility(View.VISIBLE);
                Verify.setVisibility(View.INVISIBLE);


                Toast.makeText(PhoneActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


                Toast.makeText(PhoneActivity.this, "Code has been sent", Toast.LENGTH_SHORT).show();


                PhoneNumber.setText("");
                PhoneNumber.setVisibility(View.INVISIBLE);
                VerificationMassage.setVisibility(View.VISIBLE);
                TextMassage.setVisibility(View.INVISIBLE);
                TextMassage2.setVisibility(View.VISIBLE);
                SendMassage.setVisibility(View.INVISIBLE);
                Verify.setVisibility(View.VISIBLE);

            }
        };






        }





    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        loadingBar.dismiss();
                        SendUserToMainActivity();
                        }
                        else {
                            String er = task.getException().toString();
                            Toast.makeText(PhoneActivity.this, er, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(PhoneActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}
