package com.example.ahmed.thebakingapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmed.thebakingapp.Fragments.MediaFragment;
import com.example.ahmed.thebakingapp.R;

public class MediaActivity extends AppCompatActivity {

    int currentStepId;
    String desc;
    String stepUrlstr;
    String thumbUrlstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        Intent intent = getIntent();
        currentStepId = intent.getIntExtra("STEP_ID",0);
        desc = intent.getStringExtra("DESC");
        stepUrlstr = intent.getStringExtra("STEP_URL");
        thumbUrlstr = intent.getStringExtra("THUMB_URL");

        Bundle bundle = new Bundle();
        bundle.putInt("STEP_ID", currentStepId);
        bundle.putString("DESC", desc);
        bundle.putString("STEP_URL", stepUrlstr);
        bundle.putString("THUMB_URL", thumbUrlstr);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            MediaFragment mediavideoFragment = new MediaFragment();
            mediavideoFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.mediaConainer, mediavideoFragment)
                    .commit();
        }

    }
}
