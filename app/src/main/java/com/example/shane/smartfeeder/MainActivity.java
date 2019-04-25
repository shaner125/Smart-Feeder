package com.example.shane.smartfeeder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnProgram, btnOpenStream, btnLogout, btnViewClips, btnPetRecognition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        // set the view now
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnProgram = (Button) findViewById(R.id.btn_program_feeding_schedule);
        btnOpenStream = (Button) findViewById(R.id.btn_open_live_stream);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnViewClips = (Button) findViewById(R.id.btn_ViewClips);
        btnPetRecognition = (Button) findViewById(R.id.btn_petRecognition);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProgramSchedule.class));
            }
        });

        btnOpenStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,activity_livestream.class));
            }
        });

        btnViewClips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewStoredClips.class));
            }
        });
//
//        btnPetRecognition.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, PetRecognition.class));
//            }
//        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }});
    }
}