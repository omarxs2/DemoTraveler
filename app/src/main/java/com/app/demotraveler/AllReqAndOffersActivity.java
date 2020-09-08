package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AllReqAndOffersActivity extends AppCompatActivity {

    private Toolbar mToolbat;
    private TabHost tabHost;
    private TabHost.TabSpec tabSpec;

    private ImageButton Add;

    private DatabaseReference ReqRef,UserRef,OffersRef;
    String CurrentUserID;
    private FirebaseAuth mAuth;
    private RecyclerView AllReq,AllOff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_req_and_offers);


        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabSpec = tabHost.newTabSpec("Screen-1");
        tabSpec.setContent(R.id.my_req);
        tabSpec.setIndicator("Requests Sent", null);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("Screen-2");
        tabSpec.setContent(R.id.my_off);
        tabSpec.setIndicator("Offers Received", null);
        tabHost.addTab(tabSpec);



        mToolbat= (Toolbar) findViewById(R.id.all_req_off);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("Local Guid");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Add=findViewById(R.id.add_req);


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            CurrentUserID = mAuth.getCurrentUser().getUid();
        }

        ReqRef = FirebaseDatabase.getInstance().
                getReference().child("Users").child(CurrentUserID).child("Requests Sent");


        OffersRef= FirebaseDatabase.getInstance().
                getReference().child("Users").child(CurrentUserID).child("Requests Received");


        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");


        AllReq=findViewById(R.id.all_req_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        AllReq.setHasFixedSize(true);
        AllReq.setLayoutManager(linearLayoutManager);

        AllOff =findViewById(R.id.all_off_list);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        AllOff.setHasFixedSize(true);
        AllOff.setLayoutManager(linearLayoutManager2);










        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLocalGuidActivity();
            }
        });




        ShowRequest();

        ShowOffers();


    }







    private void ShowRequest(){



        FirebaseRecyclerOptions<Requests> options =
                new FirebaseRecyclerOptions.Builder<Requests>()
                        .setQuery(ReqRef, Requests.class)
                        .build();




        FirebaseRecyclerAdapter<Requests, AllReqAndOffersActivity.RequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Requests, RequestsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestsViewHolder holder, int i, @NonNull Requests requests) {

                        final String ReqID = getRef(i).getKey();


                        ReqRef.child(ReqID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                if (dataSnapshot.exists()){

                                    String startingdate=dataSnapshot.child("StartingDate").getValue().toString();
                                    String endinggdate=dataSnapshot.child("EndingDate").getValue().toString();
                                    String nopeople=dataSnapshot.child("PeopleNo").getValue().toString();
                                    String notes=dataSnapshot.child("Notes").getValue().toString();
                                    String status=dataSnapshot.child("RequestStatus").getValue().toString();
                                    String payment=dataSnapshot.child("Payment").getValue().toString();

                                    final String guideID=dataSnapshot.child("GuideID").getValue().toString();


                                    if (status.equals("Accepted")){
                                        holder.Status.setVisibility(View.INVISIBLE);
                                        holder.Declined.setVisibility(View.INVISIBLE);
                                        holder.Accepted.setVisibility(View.VISIBLE);
                                        holder.PayNow.setVisibility(View.VISIBLE);
                                        if (payment.equals("Done")){
                                            holder.PayNow.setBackgroundResource(R.drawable.buttons);
                                            holder.PayNow.setText("Paid");
                                            holder.PayNow.setEnabled(false);


                                        }


                                    }
                                    else if (status.equals("Declined")){
                                        holder.Status.setVisibility(View.INVISIBLE);
                                        holder.Declined.setVisibility(View.VISIBLE);
                                        holder.Accepted.setVisibility(View.INVISIBLE);
                                        holder.PayNow.setVisibility(View.INVISIBLE);

                                    }

                                    else if (status.equals("Paid")){
                                        holder.Status.setVisibility(View.INVISIBLE);
                                        holder.Accepted.setVisibility(View.VISIBLE);
                                        holder.Declined.setVisibility(View.INVISIBLE);
                                        holder.Accepted.setText("Paid");
                                        holder.PayNow.setVisibility(View.VISIBLE);
                                        holder.PayNow.setBackgroundResource(R.drawable.buttons);
                                        holder.PayNow.setText("Paid");
                                        holder.PayNow.setEnabled(false);
                                        holder.Delete.setEnabled(false);


                                    }
                                    else{

                                        holder.Status.setVisibility(View.VISIBLE);
                                        holder.Declined.setVisibility(View.INVISIBLE);
                                        holder.Accepted.setVisibility(View.INVISIBLE);
                                        holder.PayNow.setVisibility(View.INVISIBLE);

                                    }


                                    holder.EndDate.setText(endinggdate);
                                    holder.StartDate.setText(startingdate);
                                    holder.NoPeople.setText(nopeople);
                                    holder.Notes.setText(notes);
                                    holder.Status.setText(status);


                                    holder.Delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            UserRef.child(guideID).child("Requests Received").child(ReqID).removeValue();
                                            UserRef.child(CurrentUserID).child("Requests Sent").child(ReqID).removeValue();

                                        }
                                    });

                                    UserRef.child(guideID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                String fullName=dataSnapshot.child("FullName").getValue().toString();

                                                holder.Name.setText(fullName);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                    holder.PayNow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Toast.makeText(AllReqAndOffersActivity.this, "PaymentActivity", Toast.LENGTH_SHORT).show();
                                            //SendUserToPaymentActivity();
                                            Intent mainIntent = new Intent(AllReqAndOffersActivity.this, PaymentActivity.class);
                                            mainIntent.putExtra("ReqId",ReqID);
                                            mainIntent.putExtra("UserId",CurrentUserID);

                                            startActivity(mainIntent);
                                        }


                                    });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                    }

                    @NonNull
                    @Override
                    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_req_offers_layout,parent,false);
                        AllReqAndOffersActivity.RequestsViewHolder viewHolder=new AllReqAndOffersActivity.RequestsViewHolder (view);
                        return viewHolder;
                    }
                };

            adapter.startListening();
            AllReq.setAdapter(adapter);

    }






    private void ShowOffers(){


        FirebaseRecyclerOptions<Offers> options =
                new FirebaseRecyclerOptions.Builder<Offers>()
                        .setQuery(OffersRef, Offers.class)
                        .build();




        FirebaseRecyclerAdapter<Offers, AllReqAndOffersActivity.OffersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Offers, OffersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final OffersViewHolder holder, int i, @NonNull Offers offers) {

                        final String ReqID = getRef(i).getKey();


                        OffersRef.child(ReqID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    String startingdate=dataSnapshot.child("StartingDate").getValue().toString();
                                    String endinggdate=dataSnapshot.child("EndingDate").getValue().toString();
                                    String nopeople=dataSnapshot.child("PeopleNo").getValue().toString();
                                    String notes=dataSnapshot.child("Notes").getValue().toString();
                                    String status=dataSnapshot.child("RequestStatus").getValue().toString();
                                    final String SenderID=dataSnapshot.child("SenderID").getValue().toString();


                                    if (status.equals("Accepted")){
                                        holder.Status.setVisibility(View.INVISIBLE);
                                        holder.Declined.setVisibility(View.INVISIBLE);
                                        holder.Accepted.setVisibility(View.VISIBLE);
                                        holder.Accepted.setText("Accepted");

                                    }
                                    else if (status.equals("Declined")){
                                        holder.Status.setVisibility(View.INVISIBLE);
                                        holder.Declined.setVisibility(View.VISIBLE);
                                        holder.Accepted.setVisibility(View.INVISIBLE);
                                    }
                                    else if (status.equals("Paid")){
                                        holder.Status.setVisibility(View.INVISIBLE);
                                        holder.Accepted.setVisibility(View.VISIBLE);
                                        holder.Declined.setVisibility(View.INVISIBLE);
                                        holder.Accepted.setText("Paid");
                                        holder.Accept.setEnabled(false);
                                        holder.Decline.setEnabled(false);

                                    }
                                    else{

                                        holder.Status.setVisibility(View.VISIBLE);
                                        holder.Status.setText("Received");
                                        holder.Declined.setVisibility(View.INVISIBLE);
                                        holder.Accepted.setVisibility(View.INVISIBLE);

                                    }



                                    holder.EndDate.setText(endinggdate);
                                    holder.StartDate.setText(startingdate);
                                    holder.NoPeople.setText(nopeople);
                                    holder.Notes.setText(notes);
                                    //holder.Status.setText(status);


                                    UserRef.child(SenderID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                String fullName=dataSnapshot.child("FullName").getValue().toString();

                                                holder.Name.setText(fullName);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    holder.Accept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            OffersRef.child(ReqID).child("RequestStatus").setValue("Accepted");
                                            UserRef.child(SenderID).child("Requests Sent")
                                                    .child(ReqID).child("RequestStatus").setValue("Accepted");



                                        }
                                    });

                                    holder.Decline.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            OffersRef.child(ReqID).child("RequestStatus").setValue("Declined");
                                            UserRef.child(SenderID).child("Requests Sent")
                                                    .child(ReqID).child("RequestStatus").setValue("Declined");


                                        }
                                    });






                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_offers_layout,parent,false);
                        AllReqAndOffersActivity.OffersViewHolder viewHolder=new AllReqAndOffersActivity.OffersViewHolder (view);
                        return viewHolder;
                    }
                };

        adapter.startListening();
        AllOff.setAdapter(adapter);

    }







    public static class RequestsViewHolder extends RecyclerView.ViewHolder{
        private TextView Name,NoPeople,StartDate,EndDate,Notes,Status,Declined,Accepted;
        private Button Delete,PayNow;


        public RequestsViewHolder(View itemView) {
            super(itemView);

            Name=itemView.findViewById(R.id.show_local_name);
            NoPeople=itemView.findViewById(R.id.show_number_people);
            StartDate=itemView.findViewById(R.id.show_startdate);
            EndDate=itemView.findViewById(R.id.show_enddate);
            Notes=itemView.findViewById(R.id.show_notes);
            Delete=itemView.findViewById(R.id.delete_req);
            Status=itemView.findViewById(R.id.show_status);
            Declined=itemView.findViewById(R.id.show_declined_status);
            Accepted=itemView.findViewById(R.id.show_accepted_status);
            PayNow=itemView.findViewById(R.id.show_pay_btn);

        }
    }


    public static class OffersViewHolder extends RecyclerView.ViewHolder{
        private TextView Name,NoPeople,StartDate,EndDate,Notes,Status,Declined,Accepted;
        private Button Accept,Decline;


        public OffersViewHolder(View itemView) {
            super(itemView);

            Name=itemView.findViewById(R.id.offer_sender_name);
            NoPeople=itemView.findViewById(R.id.offer_number_people);
            StartDate=itemView.findViewById(R.id.offer_startdate);
            EndDate=itemView.findViewById(R.id.offer_enddate);
            Notes=itemView.findViewById(R.id.offer_notes);
            Status=itemView.findViewById(R.id.offer_status);
            Declined=itemView.findViewById(R.id.offer_declined_status);
            Accepted=itemView.findViewById(R.id.offer_accepted_status);

            Accept=itemView.findViewById(R.id.offer_acceptbtn);
            Decline=itemView.findViewById(R.id.offer_declinebtn);


        }
    }





    private void SendUserToLocalGuidActivity() {
        Intent mainIntent = new Intent(AllReqAndOffersActivity.this, LocalGuidActivity.class);
        startActivity(mainIntent);
    }


    private void SendUserToPaymentActivity() {
        Intent mainIntent = new Intent(AllReqAndOffersActivity.this, PaymentActivity.class);
        startActivity(mainIntent);

    }





}
