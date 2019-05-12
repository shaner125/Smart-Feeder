package com.example.shane.smartfeeder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewStoredClips extends AppCompatActivity {

    ListView listview;
    public VideoView videoViewLandscape;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://smart-feeder-1d272.appspot.com");
    private DatabaseReference mDatabase ;
    StorageReference storageRef = storage.getReference();
    String vidURL;
    static String[] ListElements = new String[]{};
    public static List< String > ListElementsArrayList = new ArrayList< String >
            (Arrays.asList(ListElements));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stored_clips);
        videoViewLandscape= (VideoView) findViewById(R.id.videoView1);
        final ArrayAdapter< String > adapter = new ArrayAdapter < String >
                (ViewStoredClips.this, android.R.layout.simple_list_item_1,
                        ListElementsArrayList);
        listview = (ListView) findViewById(R.id.listView1);
        listview.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("medialinks");
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
                                Log.e("TAG", "" + ListElementsArrayList.toString());
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0,View arg1, int position, long arg3)
            {
                String data=(String)arg0.getItemAtPosition(position);
                Log.e("TAG", "" + data);
                storageRef.child(data).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        videoViewLandscape.setVideoURI(uri);
                        videoViewLandscape.requestFocus();
                        videoViewLandscape.start();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("TAG", "NOT WORKINGGGGGGG");
                    }
                });
            }
        });
    }
}
