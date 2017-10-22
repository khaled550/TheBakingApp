package com.example.ahmed.thebakingapp.Fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.thebakingapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.prefs.AbstractPreferences;

public class MediaFragment extends Fragment{

    String videoUrl;
    String thumbUrl;
    String description;
    int currentStepID;

    static MediaSessionCompat mediaSession;
    SimpleExoPlayer simpleExoPlayer;
    SimpleExoPlayerView simpleExoPlayerView;
    PlaybackStateCompat.Builder stateBuilder;

    TextView textViewDesc;
    ImageView thumbImg;
    Button nextVid;
    Button prevVid;
    private long position;
    private AbstractPreferences currentState;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media, container, false);

        textViewDesc = (TextView) root.findViewById(R.id.stepVideoDesc);
        simpleExoPlayerView = (SimpleExoPlayerView) root.findViewById(R.id.SimpleExoPlayerView);
        nextVid = (Button) root.findViewById(R.id.buttonNext);
        prevVid = (Button) root.findViewById(R.id.buttonPrev);
        thumbImg = (ImageView) root.findViewById(R.id.thumbImage);

        position = C.TIME_UNSET;

        Bundle bundle = this.getArguments();
        if (savedInstanceState != null) {
            if (bundle != null) {
                currentStepID = bundle.getInt("STEP_ID",0);
                description = bundle.getString("DESC");
                videoUrl = bundle.getString("STEP_URL");
                thumbUrl = bundle.getString("THUMB_URL");

                if (!thumbUrl.isEmpty()){
                    thumbImg.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity())
                            .load(thumbUrl)
                            .into(thumbImg);
                }

                textViewDesc.setText(description);

                initializePlayer(Uri.parse(videoUrl));
                initializeMediaSession();

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    nextVid.setVisibility(View.GONE);
                    prevVid.setVisibility(View.GONE);
                    textViewDesc.setVisibility(View.GONE);
                    simpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    simpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    hideSystemUI();
                }
            }            position = savedInstanceState.getLong("SELECTED_POSITION", C.TIME_UNSET);
        }

        nextVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStepID < RecipesInfoFragment.stepList.size() - 1) {
                    currentStepID++;
                    releasePlayer();
                    textViewDesc.setText(RecipesInfoFragment.stepList.get(currentStepID).getDescription());
                    initializePlayer(Uri.parse(RecipesInfoFragment.stepList.get(currentStepID).getVideoURL()));
                }
            }
        });
        prevVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStepID > 0){
                    currentStepID--;
                    releasePlayer();
                    textViewDesc.setText(RecipesInfoFragment.stepList.get(currentStepID).getDescription());
                    initializePlayer(Uri.parse(RecipesInfoFragment.stepList.get(currentStepID).getVideoURL()));
                }
            }
        });

        return root;
    }

    private void initializePlayer (Uri videoUrl){
        if (simpleExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),trackSelector,loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            String userAgent = Util.getUserAgent(getActivity(),"Step Video");
            MediaSource mediaSource = new ExtractorMediaSource(videoUrl, new DefaultDataSourceFactory(getActivity(), userAgent),new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        if (simpleExoPlayer != null) {
            position = simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    private void releasePlayer(){
        if (simpleExoPlayer != null){
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (position != C.TIME_UNSET)
            simpleExoPlayer.seekTo(position);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        currentState.putLong("SELECTED_POSITION", position);

    }

    private void initializeMediaSession() {

        mediaSession = new MediaSessionCompat(getActivity(), "MediaSession");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());

        mediaSession.setCallback(new SessionCallback());
        mediaSession.setActive(true);
    }

    private void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private class SessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            simpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }
}