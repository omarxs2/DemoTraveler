package com.app.demotraveler;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private Toolbar mToolbat;
    private EditText Email;
    private Button SendReset;
    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mToolbat= (Toolbar) findViewById(R.id.reset_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Email = (EditText) findViewById(R.id.reset_email);
        SendReset = (Button) findViewById(R.id.reset_button);

        mAuth = FirebaseAuth.getInstance();


        SendReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useremail = Email.getText().toString();

                if (TextUtils.isEmpty(useremail)){

                    Toast.makeText(ResetPasswordActivity.this, "Please write your email first..", Toast.LENGTH_SHORT).show();

                }

                else {

                    mAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "Please check your email..", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));

                            }

                            else{
                                String error = task.getException().getMessage();
                                Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
