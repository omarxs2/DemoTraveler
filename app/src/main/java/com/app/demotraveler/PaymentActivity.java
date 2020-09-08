package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {
    private Toolbar mToolbat;
    private String RequestId,userId;
    private DatabaseReference ReqRef,UserRef;






    //// https://github.com/geeckmc/CardForm/tree/master/cardform

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);



        RequestId=  getIntent().getExtras().get("ReqId").toString();

        ReqRef = FirebaseDatabase.getInstance().
                getReference().child("GuidesBooks").child("Requests").child(RequestId);


        UserRef = FirebaseDatabase.getInstance().
                getReference().child("Users");

        mToolbat= (Toolbar) findViewById(R.id.payment_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        CardForm cardForm = (CardForm) findViewById(R.id.craftform);
        Button paybtn = (Button) findViewById(R.id.btn_pay);
        paybtn.setText("Pay");

        TextView txtDos2 = (TextView) findViewById(R.id.card_preview_name);
        txtDos2.setHint("Card Holder");

        final TextView payamount = (TextView) findViewById(R.id.payment_amount_holder);

        EditText holder = (EditText) findViewById(R.id.card_name);
        holder.setHint("Card Holder");

       final TextView amount = (TextView) findViewById(R.id.payment_amount);




        ReqRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    amount.setText(dataSnapshot.child("TotalPrice").getValue().toString()+"$");
                    payamount.setText("Total amount for "
                            +dataSnapshot.child("DurationDays").getValue().toString()
                            + " days");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                String x;

                if (card.validateCard()) {
                     x = "true";
                    ReqRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String userid = dataSnapshot.child("SenderID").getValue().toString();
                            final String guideid = dataSnapshot.child("GuideID").getValue().toString();

                            final HashMap hashMap = new HashMap();

                            hashMap.put("Payment","Done");
                            hashMap.put("RequestStatus","Paid");

                            UserRef.child(userid).child("Requests Sent").child(RequestId).updateChildren(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            UserRef.child(guideid).child("Requests Received").child(RequestId).updateChildren(hashMap).
                                                    addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                         ReqRef.updateChildren(hashMap)
                                                                 .addOnSuccessListener(new OnSuccessListener() {
                                                                     @Override
                                                                     public void onSuccess(Object o) {

                                                                         Intent mainIntent = new Intent(PaymentActivity.this, AllReqAndOffersActivity.class);
                                                                         startActivity(mainIntent);
                                                                     }
                                                                 })   ;
                                                        }
                                                    });
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                else {
                    x="false";
                }

                Toast.makeText(PaymentActivity.this,x , Toast.LENGTH_SHORT).show();

            }
        });


    }
}
