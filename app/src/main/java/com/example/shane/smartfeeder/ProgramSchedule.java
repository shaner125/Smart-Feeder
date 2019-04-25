package com.example.shane.smartfeeder;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProgramSchedule extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase ;
    ListView listview;
    static String[] ListElements = new String[]{};
    TimePicker picker;
    Button btnGet, btnAddTime, btnRemoveTime, btnBack;
    String tvw;
    static int previousHour, previousMin;
    public static List< String > ListElementsArrayList = new ArrayList< String >
            (Arrays.asList(ListElements));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_schedule);

        final ArrayAdapter< String > adapter = new ArrayAdapter < String >
                (ProgramSchedule.this, android.R.layout.simple_list_item_1,
                        ListElementsArrayList);

        auth = FirebaseAuth.getInstance();
        btnGet=(Button)findViewById(R.id.button1);
        btnGet.setVisibility(View.GONE);
        btnBack=(Button)findViewById(R.id.btn_back);
        btnAddTime=(Button)findViewById(R.id.btn_addTime);
        btnRemoveTime=(Button)findViewById(R.id.btn_removeTime);
        picker=(TimePicker)findViewById(R.id.timePicker1);
        picker.setIs24HourView(true);
        listview = (ListView) findViewById(R.id.listView1);
        listview.setAdapter(adapter);

        picker.setVisibility(View.GONE);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("data").child(auth.getUid()).child("feeding_schedule");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            Log.e("TAG", "" + snapshot.getValue()); // your name values you will get here
                            ListElementsArrayList.clear();
                            for(DataSnapshot child : snapshot.getChildren() ){
                                        ListElementsArrayList.add(child.getValue().toString());
                                        adapter.notifyDataSetChanged();
                                    }

                            if(ListElementsArrayList.isEmpty()){
                                btnRemoveTime.setVisibility(View.GONE);
                                Log.e("TAG", "button gone");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        btnRemoveTime.setVisibility(View.GONE);
                        Log.e("TAG", " it's null. dumb dumb");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("onCancelled", " cancelled");
            }

        });

        btnRemoveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try{
                if(ListElementsArrayList.size() == 1){
                    mDatabase.child("1").setValue(null);
                    ListElementsArrayList.clear();
                    adapter.notifyDataSetChanged();
                    btnRemoveTime.setVisibility(View.GONE);
                }
                else {
                    mDatabase.child("" + ListElementsArrayList.size()).setValue(null);
                }
            }
            catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }

            }
        });

        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setVisibility((listview.getVisibility() == View.VISIBLE)
                        ? View.GONE : View.VISIBLE);
                btnAddTime.setVisibility((btnAddTime.getVisibility() == View.VISIBLE)
                        ? View.GONE : View.VISIBLE);
                btnBack.setVisibility((btnBack.getVisibility() == View.VISIBLE)
                        ? View.GONE : View.VISIBLE);
                btnRemoveTime.setVisibility((btnRemoveTime.getVisibility() == View.VISIBLE)
                        ? View.GONE : View.GONE);
                btnGet.setVisibility((btnGet.getVisibility() == View.INVISIBLE)
                        ? View.GONE : View.VISIBLE);
                picker.setVisibility((btnGet.getVisibility() == View.INVISIBLE)
                        ? View.GONE : View.VISIBLE);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;

                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = picker.getHour();
                    minute = picker.getMinute();
                }
                else{
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }


                boolean exists = false;

                if ((hour<10) && (minute<10)){
                    tvw = "0"+hour+":0"+minute;
                }
                else if ((hour<10) && (minute>10)){
                    tvw = "0"+hour+":"+minute;
                }
                else if((hour>10) && (minute<10)){
                    tvw = hour+":0"+minute;
                }
                else{
                    tvw = hour+":"+minute;
                }

                for(String match : ListElementsArrayList){
                    if (match.equals(tvw)){
                        Log.e("TAG", "That feed time already exists!");
                        Toast.makeText(ProgramSchedule.this, "That Feed Time Already Exists!", Toast.LENGTH_SHORT).show();
                        exists = true;
                        break;
                    }
                }
                if (!exists){
                    if (ListElementsArrayList.size()>0) {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        try {
                            Date previousTime = sdf.parse(ListElementsArrayList.get(ListElementsArrayList.size() - 1));
                            Date currentTime = sdf.parse(tvw);

                            if (previousTime.getTime() > currentTime.getTime()) {
                                Log.e("TAG", "Please restructure feeding times!");
                                Toast.makeText(ProgramSchedule.this, "Please restructure feeding times from earliest to latest!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("TAG", "Adding feed time!");
                                mDatabase.child("" + (ListElementsArrayList.size() + 1)).setValue(tvw);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Log.e("TAG", "Adding feed time!");
                        mDatabase.child("" + (ListElementsArrayList.size() + 1)).setValue(tvw);
                    }
                }

                listview.setVisibility((listview.getVisibility() == View.INVISIBLE)
                        ? View.GONE : View.VISIBLE);
                btnAddTime.setVisibility((btnAddTime.getVisibility() == View.INVISIBLE)
                        ? View.GONE : View.VISIBLE);
                btnBack.setVisibility((btnBack.getVisibility() == View.INVISIBLE)
                        ? View.GONE : View.VISIBLE);
                btnRemoveTime.setVisibility((btnAddTime.getVisibility() == View.INVISIBLE)
                        ? View.GONE : View.VISIBLE);
                btnGet.setVisibility((btnGet.getVisibility() == View.VISIBLE)
                        ? View.GONE : View.INVISIBLE);
                picker.setVisibility((btnGet.getVisibility() == View.VISIBLE)
                        ? View.GONE : View.INVISIBLE);
            }
        });


    }
}