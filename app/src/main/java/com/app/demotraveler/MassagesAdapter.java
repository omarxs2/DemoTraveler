package com.app.demotraveler;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class  MassagesAdapter extends RecyclerView.Adapter<MassagesAdapter.MassageViewHolder> {

    private List<Massages> userMassagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;


    public MassagesAdapter (List<Massages> userMassagesList){

        this.userMassagesList=userMassagesList;
    }





    public class MassageViewHolder extends RecyclerView.ViewHolder  {

        public TextView SenderMassageText,ReceiverMassageText;
        public CircleImageView ReceiverImage;

        public MassageViewHolder(@NonNull View itemView) {
            super(itemView);

            SenderMassageText=(TextView) itemView.findViewById(R.id.sender_massage_text);
            ReceiverMassageText=(TextView) itemView.findViewById(R.id.receiver_massage_text);
            ReceiverImage=(CircleImageView) itemView.findViewById(R.id.massage_profile_image);

        }
    }


    @NonNull
    @Override
    public MassageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View V = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.massage_layout_of_users,viewGroup,false);

        mAuth=FirebaseAuth.getInstance();

        return new MassageViewHolder(V);
    }


    @Override
    public void onBindViewHolder(@NonNull final MassageViewHolder massageViewHolder, int i) {


        String massageSenderID =mAuth.getCurrentUser().getUid();
        Massages massages = userMassagesList.get(i);

        String fromUserID = massages.getSender();
        String fromMassageType = massages.getType();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(fromUserID);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    String image = dataSnapshot.child("ProfileImage").getValue().toString();



                    Picasso.get().load(image).placeholder(R.drawable.profile).into(massageViewHolder.ReceiverImage);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        if (fromMassageType.equals("text")){

            massageViewHolder.ReceiverMassageText.setVisibility(View.INVISIBLE);
            massageViewHolder.ReceiverImage.setVisibility(View.INVISIBLE);


            if (fromUserID.equals(massageSenderID)){

                massageViewHolder.SenderMassageText.setBackgroundResource(R.drawable.sender_massage_backg);
                massageViewHolder.SenderMassageText.setTextColor(Color.BLACK);
                massageViewHolder.SenderMassageText.setGravity(Gravity.LEFT);
                massageViewHolder.SenderMassageText.setText(massages.getMassage());



            }
            else{
                massageViewHolder.SenderMassageText.setVisibility(View.INVISIBLE);
                massageViewHolder.ReceiverMassageText.setVisibility(View.VISIBLE);
                massageViewHolder.ReceiverImage.setVisibility(View.VISIBLE);

                massageViewHolder.ReceiverMassageText.setBackgroundResource(R.drawable.reciever_massage_backg);
                massageViewHolder.ReceiverMassageText.setTextColor(Color.BLACK);
                massageViewHolder.ReceiverMassageText.setGravity(Gravity.LEFT);
                massageViewHolder.ReceiverMassageText.setText(massages.getMassage());

            }
        }

    }

    @Override
    public int getItemCount() {



        return userMassagesList.size();
    }
}


