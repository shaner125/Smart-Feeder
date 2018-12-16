package com.example.shane.smartfeeder;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

public class ViewStoredClips extends AppCompatActivity {

    public VideoView videoViewLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stored_clips);
        videoViewLandscape= (VideoView) findViewById(R.id.videoView1);
        String str = "https://firebasestorage.googleapis.com/v0/b/smart-feeder-1d272.appspot.com/o/Ragdoll%20kittens%20at%20play.mp4?alt=media&token=b9385aa5-b4fe-474a-8dc1-4264aeffe23f";
        Uri uri = Uri.parse(str);
        videoViewLandscape.setVideoURI(uri);
//        progressBarLandScape.setVisibility(View.VISIBLE);
        videoViewLandscape.requestFocus();
        videoViewLandscape.start();
    }
}
