package com.app.demotraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

public class BookLocalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final String TAG = "BookLocalActivity";

    private TextView DateStart,DateEnd;
    private EditText Notes;
    private Spinner spinner;
    String PeopleNo,curentUserID,LocalID,StartingDate,EndingDate,UserNotes;
    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetListener2;
    private Button SendReq;

    private int price;

    private DatabaseReference LocalRef,UserRef,LocalBooksRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_local);

        DateStart = (TextView) findViewById(R.id.date_start);
        DateEnd = (TextView) findViewById(R.id.date_end);
        Notes = (EditText) findViewById(R.id.notes);
        SendReq = (Button) findViewById(R.id.send_request_btn);



        spinner = findViewById(R.id.spinner_people_no);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.PeopleNoList,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(BookLocalActivity.this);




        mAuth=FirebaseAuth.getInstance();

        curentUserID=mAuth.getCurrentUser().getUid();
        LocalID=  getIntent().getExtras().get("UserKey").toString();
        price = parseInt(getIntent().getExtras().get("Price").toString()) ;


        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        LocalBooksRef= FirebaseDatabase.getInstance().getReference().child("GuidesBooks");







        DateStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                BookLocalActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDateSetListener,
                                year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });

                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                        StartingDate = day + "/" + month + "/" + year;
                        DateStart.setText(StartingDate);
                    }
                };



        DateEnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                BookLocalActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDateSetListener2,
                                year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });

                 mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                        EndingDate = day + "/" + month + "/" + year;
                        DateEnd.setText(EndingDate);
                    }
                };




                 SendReq.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Calendar calDate = Calendar.getInstance();
                         SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                         String saveCurrentDate = currentDate.format(calDate.getTime());

                         Calendar calTime = Calendar.getInstance();
                         SimpleDateFormat currentTime = new SimpleDateFormat("HH-mm-ss");
                         String saveCurrentTime = currentTime.format(calTime.getTime());

                         final String RequestID=curentUserID+saveCurrentTime+saveCurrentDate+"Request";


                         String sDate1=StartingDate;
                         Date date1 = null;

                         String sDate2=EndingDate;
                         Date date2 = null;


                         try {
                             date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                         } catch (ParseException e) {
                             // TODO Auto-generated catch block
                             e.printStackTrace();
                         }


                         try {
                             date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
                         } catch (ParseException e) {
                             // TODO Auto-generated catch block
                             e.printStackTrace();
                         }
                         System.out.println(sDate1+"\t"+date1);
                         System.out.println(sDate2+"\t"+date2);

                         long diffInMillies = date2.getTime() - date1.getTime();
                         long duration = TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS);





                        long totalprice = price * duration;


                         UserNotes = Notes.getText().toString();

                         final HashMap PostsMap = new HashMap();
                         PostsMap.put("GuideID",LocalID);
                         PostsMap.put("PeopleNo",PeopleNo);
                         PostsMap.put("StartingDate",StartingDate);
                         PostsMap.put("EndingDate",EndingDate);
                         PostsMap.put("Notes",UserNotes);
                         PostsMap.put("RequestStatus","Request Sent");
                         PostsMap.put("SenderID", curentUserID);
                         PostsMap.put("Payment", "Not Yet");
                         PostsMap.put("DurationDays", duration );
                         PostsMap.put("TotalPrice", totalprice);





                         LocalBooksRef.child("Requests").child(RequestID).updateChildren(PostsMap);

                         UserRef.child(curentUserID).child("Requests Sent").child(RequestID).updateChildren(PostsMap).addOnCompleteListener(new OnCompleteListener() {
                             @Override
                             public void onComplete(@NonNull Task task) {

                                 if (task.isSuccessful()) {
                                     /*
                                     HashMap PostsMap2 = new HashMap();
                                     PostsMap2.put("SenderID", curentUserID);
                                     PostsMap2.put("PeopleNumber", PeopleNo);
                                     PostsMap2.put("StartingDate", StartingDate);
                                     PostsMap2.put("EndingDate", EndingDate);
                                     PostsMap2.put("Notes", UserNotes);
                                     PostsMap2.put("RequestStatus", "Request Sent");
                                     PostsMap2.put("GuideID",LocalID);
*/

                                     UserRef.child(LocalID).child("Requests Received").child(RequestID).updateChildren(PostsMap).addOnCompleteListener(new OnCompleteListener() {
                                         @Override
                                         public void onComplete(@NonNull Task task) {
                                             if (task.isSuccessful()) {
                                                 Toast.makeText(BookLocalActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                                                 SendUserToAllReqAndOffersActivity();

                                             }
                                         }
                                     });

                                 }
                             }
                         });

                     }
                 });













    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getItemAtPosition(position).equals("Select")){

            Toast.makeText(this, "Please choose number of people", Toast.LENGTH_SHORT).show();
        }
        else{

            PeopleNo = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    private void SendUserToAllReqAndOffersActivity() {
        Intent mainIntent = new Intent(BookLocalActivity.this, AllReqAndOffersActivity.class);
        startActivity(mainIntent);
    }









}
