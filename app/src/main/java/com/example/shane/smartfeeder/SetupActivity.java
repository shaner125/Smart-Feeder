package com.example.shane.smartfeeder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetupActivity extends AppCompatActivity {

    Button btnBack, btn_dog_on, btn_dog_off, btn_cat_on, btn_cat_off, btn_smallOn, btn_smallOff, btn_mediumOn, btn_mediumOff, btn_largeOn, btn_largeOff;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("data").child("user");

        btnBack = (Button)findViewById(R.id.btn_back);

        btn_dog_on = (Button) findViewById(R.id.btn_dog_on);
        btn_dog_off = (Button) findViewById(R.id.btn_dog_off);
        btn_cat_on = (Button) findViewById(R.id.btn_cat_on);
        btn_cat_off = (Button) findViewById(R.id.btn_cat_off);

        btn_smallOn = (Button) findViewById(R.id.btn_smallOn);
        btn_smallOff = (Button) findViewById(R.id.btn_smallOff);
        btn_mediumOn = (Button) findViewById(R.id.btn_mediumOn);
        btn_mediumOff = (Button) findViewById(R.id.btn_mediumOff);
        btn_largeOn = (Button) findViewById(R.id.btn_largeOn);
        btn_largeOff = (Button) findViewById(R.id.btn_largeOff);

        btn_cat_on.setVisibility(View.GONE);
        btn_cat_off.setVisibility(View.GONE);
        btn_dog_on.setVisibility(View.GONE);
        btn_dog_off.setVisibility(View.GONE);
        btn_smallOn.setVisibility(View.GONE);
        btn_smallOff.setVisibility(View.GONE);
        btn_mediumOff.setVisibility(View.GONE);
        btn_mediumOn.setVisibility(View.GONE);
        btn_largeOn.setVisibility(View.GONE);
        btn_largeOff.setVisibility(View.GONE);

        btn_cat_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("pet_type").setValue("cat");
            }
        });

        btn_dog_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("pet_type").setValue("dog");
            }
        });

        btn_smallOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("feeding_size").setValue("1");
            }
        });

        btn_mediumOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("feeding_size").setValue("2");
            }
        });

        btn_largeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("feeding_size").setValue("3");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            for(DataSnapshot child : snapshot.getChildren() ){
                                if (child.toString().contains("feeding_size")){
                                    int size = Integer.parseInt(child.getValue().toString());
                                    if (size == 1){
                                        btn_smallOn.setVisibility(View.VISIBLE);
                                        btn_smallOff.setVisibility(View.GONE);
                                        btn_mediumOff.setVisibility(View.VISIBLE);
                                        btn_mediumOn.setVisibility(View.GONE);
                                        btn_largeOff.setVisibility(View.VISIBLE);
                                        btn_largeOn.setVisibility(View.GONE);
                                    }
                                    if (size == 2){
                                        btn_smallOn.setVisibility(View.GONE);
                                        btn_smallOff.setVisibility(View.VISIBLE);
                                        btn_mediumOff.setVisibility(View.GONE);
                                        btn_mediumOn.setVisibility(View.VISIBLE);
                                        btn_largeOff.setVisibility(View.VISIBLE);
                                        btn_largeOn.setVisibility(View.GONE);
                                    }
                                    if (size == 3){
                                        btn_smallOn.setVisibility(View.GONE);
                                        btn_smallOff.setVisibility(View.VISIBLE);
                                        btn_mediumOff.setVisibility(View.VISIBLE);
                                        btn_mediumOn.setVisibility(View.GONE);
                                        btn_largeOff.setVisibility(View.GONE);
                                        btn_largeOn.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (child.toString().contains("pet_type")){
                                    String type = child.getValue().toString();
                                    if (type.contains("cat")){
                                        btn_dog_off.setVisibility(View.VISIBLE);
                                        btn_dog_on.setVisibility(View.GONE);
                                        btn_cat_off.setVisibility(View.GONE);
                                        btn_cat_on.setVisibility(View.VISIBLE);
                                    }
                                    if (type.contains("dog")){
                                        btn_dog_off.setVisibility(View.GONE);
                                        btn_dog_on.setVisibility(View.VISIBLE);
                                        btn_cat_off.setVisibility(View.VISIBLE);
                                        btn_cat_on.setVisibility(View.GONE);
                                    }

                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
    }
}
