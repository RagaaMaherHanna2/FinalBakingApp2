package com.example.marian.finalbakingapp2.fragment;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marian.finalbakingapp2.R;
import com.example.marian.finalbakingapp2.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.marian.finalbakingapp2.activity.StepActivity.PANES;
import static com.example.marian.finalbakingapp2.activity.StepActivity.POSITION;
import static com.example.marian.finalbakingapp2.adapter.StepAdapter.STEPS;

public class IngredientAndStepDetailFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener
{


    private TrackSelector trackSelector;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;
    private TextView mDescription;
    private Button mPrevious;
    private Button mNext;
    private ImageView mImageView;
    private ArrayList<Step> steps;
    private int mIndex;
    private int mPosition;
    private boolean TwoPane;
    private static long wPosition = 0;

    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private boolean autoPlay = true;
    private int currentWindow;
    private long playbackPosition;


    public IngredientAndStepDetailFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        if (savedInstanceState != null)
        {

            steps = savedInstanceState.getParcelableArrayList(STEPS);
            TwoPane = savedInstanceState.getBoolean(PANES);
            mPosition = savedInstanceState.getInt(POSITION);

            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION,0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX,0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, true);
        }

        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);

        mNext = (Button) view.findViewById(R.id.bt_next);
        mPrevious = (Button) view.findViewById(R.id.bt_previous);

        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.sepv_step_video);

        mDescription = (TextView) view.findViewById(R.id.tv_description);
        mImageView = (ImageView)view.findViewById(R.id.iv_thumbnail);


        mNext.setOnClickListener(this);
        mPrevious.setOnClickListener(this);


        mPosition = getArguments().getInt(POSITION);
        mIndex = mPosition ;
        TwoPane = getArguments().getBoolean(PANES);


        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (TwoPane)
        {
            theTab();
        }
        else
            {
            thePhone();
        }
    }


    private void playVideo(int index)
    {

        simpleExoPlayerView.setVisibility(View.VISIBLE);
        simpleExoPlayerView.requestFocus();

        String videoUrl = steps.get(index).getVideoUrl();
        String thumbNailUrl = steps.get(index).getThumbnailUrl();
        if (!videoUrl.isEmpty())
        {
            initPlayer(Uri.parse(videoUrl));

        }
        else if (!thumbNailUrl.isEmpty())
        {
            Picasso.with(getContext())
                    .load(Uri.parse(thumbNailUrl))
                    .error(R.mipmap.ic_launcher).into(mImageView);
            mImageView.setVisibility(View.VISIBLE);


        }
        else
        {
            simpleExoPlayerView.setVisibility(View.GONE);
        }
    }

    public void theTab() {
        simpleExoPlayerView.setVisibility(View.VISIBLE);
        mDescription.setVisibility(View.VISIBLE);
        steps = getArguments().getParcelableArrayList(STEPS);
        assert steps != null;
        mDescription.setText(steps.get(mIndex).getDescription());
        playVideo(mIndex);
    }


    private void thePhone() {
        theTab();
        isLandscape();
        mPrevious.setVisibility(View.VISIBLE);
        mNext.setVisibility(View.VISIBLE);
    }

    void isLandscape() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            hideSystemUi();
    }



    private void initPlayer(Uri mediaUri)  {
        simpleExoPlayer = null;

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(),
                null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF);

        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);


        simpleExoPlayer.addListener(this);

        simpleExoPlayerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setPlayWhenReady(true);

        simpleExoPlayer.seekTo(currentWindow, playbackPosition);

        String userAgent = Util.getUserAgent(getActivity(), "Baking App");

        MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                new DefaultDataSourceFactory(getActivity(), BANDWIDTH_METER,
                        new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER)),
                new DefaultExtractorsFactory(), null, null);

        simpleExoPlayer.prepare(mediaSource);
        CheckPlayer(wPosition, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        workPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
        workPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        workPlayer();
    }


    private void workPlayer()
    {
        if (simpleExoPlayer != null)
        {

            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_next:
                if (mIndex < steps.size() - 1)
                {
                    int index = ++mIndex;
                    mDescription.setText(steps.get(index).getDescription());
                    playVideo(index);
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.end_of_steps, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_previous:
                if (mIndex > 0)
                {
                    int index = --mIndex;
                    mDescription.setText(steps.get(index).getDescription());
                    playVideo(index);
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.start_of_steps, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void CheckPlayer(long position, boolean playWhenReady) {
        this.wPosition = position;
        simpleExoPlayer.seekTo(position);
        simpleExoPlayer.setPlayWhenReady(playWhenReady);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        if (simpleExoPlayer != null)
        {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
        outState.putParcelableArrayList(STEPS, steps);
        outState.putBoolean(PANES, TwoPane);

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading)
    {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
    {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady)
        {
            Toast.makeText(getActivity(), "Working", Toast.LENGTH_LONG).show();
        }
        else if ((playbackState == ExoPlayer.STATE_READY))
        {
        }
        if (playbackState == PlaybackStateCompat.STATE_PLAYING)
        {
            wPosition = simpleExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException e)
    {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER)
        {
            Exception cause = e.getRendererException();

            if (cause instanceof MediaCodecRenderer.DecoderInitializationException)
            {
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;

                if (decoderInitializationException.decoderName == null)
                {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException)
                    {
                        errorString = getString(R.string.error_querying_decoders);
                    }
                    else if (decoderInitializationException.secureDecoderRequired)
                    {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    }
                    else
                    {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                }
                else
                {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null)
        {
            Toast.makeText(getActivity(), errorString, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPositionDiscontinuity()
    {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters)
    {
    }


}

