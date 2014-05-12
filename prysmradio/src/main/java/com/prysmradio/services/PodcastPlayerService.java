package com.prysmradio.services;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.prysmradio.PrysmApplication;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdateEpisodeProgressEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.bus.events.UpdatePodcastTitleEvent;
import com.prysmradio.utils.Constants;

import java.io.IOException;

/**
 * Created by fxoxe_000 on 21/04/2014.
 */
public class PodcastPlayerService extends PrysmAudioService implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener {

    private static final String MP3_EXTENSION = ".mp3";


    private MediaPlayer mMediaPlayer = null;
    private Handler progressHandler;
    private Runnable progressChecker;

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setVolume(0,5f);

        progressHandler = new Handler();
        progressChecker = new Runnable() {
            @Override
            public void run() {
                retrieveProgress();
                progressHandler.postDelayed(progressChecker, 1000);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceID = startId;
        String action = intent.getAction();
        ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
        progressHandler.removeCallbacks(progressChecker);
        if (action.equals(Constants.START_PODCAST_SERVICE_ACTION)) {

            if (audioUrl == null || !audioUrl.equals(intent.getStringExtra(Constants.AUDIO_URL_EXTRA))){

                audioUrl = intent.getStringExtra(Constants.AUDIO_URL_EXTRA);

                String podcastTitle = intent.getStringExtra(Constants.PODCAST_TITLE_EXTRA);
                String episodeTitle = intent.getStringExtra(Constants.EPISODE_TITLE_EXTRA);
                UpdatePodcastTitleEvent event = new UpdatePodcastTitleEvent(podcastTitle, episodeTitle);
                BusManager.getInstance().getBus().post(event);

                if (state == STATE.PREPARING) {
                    state = STATE.SHOULD_LOAD_URL;
                } else {
                    start();
                }
            } else {
                progressChecker.run();
            }
        } else if (action.equals(Constants.STOP_PODCAST_SERVICE_ACTION)) {
            if (state == STATE.PREPARING) {
                state = STATE.SHOULD_QUIT;
            } else {
                stop();
            }
        } else if (action.equals(Constants.SEEK_PODCAST_SERVICE_ACTION)){
            int seekTo = intent.getIntExtra(Constants.SEEK_TO_EXTRA, -1);
            if (state == STATE.PLAYING && mMediaPlayer.isPlaying() && seekTo != -1){
                mMediaPlayer.seekTo(seekTo);
                BusManager.getInstance().getBus().post(new UpdatePlayerEvent(false, true));
            }
        }

        return START_NOT_STICKY;
    }

    protected synchronized void start() {
        super.start();

        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                try {
                    if (mMediaPlayer.isPlaying()){
                        mMediaPlayer.stop();
                        mMediaPlayer.reset();
                    }
                    mMediaPlayer.setDataSource(audioUrl);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e){
                    Log.e(getPackageName(), e.getMessage());
                }
        }
    }

    protected synchronized void stop() {
        state = STATE.STOPPED;
        progressHandler.removeCallbacks(progressChecker);
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }


        super.stop();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (state == STATE.SHOULD_QUIT || state == STATE.SHOULD_LOAD_URL){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            if (state == STATE.SHOULD_LOAD_URL){
                start();
            } else {
                stop();
            }
            return;
        }

        mMediaPlayer.start();
        state = STATE.PLAYING;
        progressChecker.run();
        UpdatePlayerEvent event = new UpdatePlayerEvent(true, false);
        event.setDuration(mMediaPlayer.getDuration());
        BusManager.getInstance().getBus().post(event);
    }

    private void retrieveProgress(){
        UpdateEpisodeProgressEvent event = new UpdateEpisodeProgressEvent(mMediaPlayer.getCurrentPosition());
        BusManager.getInstance().getBus().post(event);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mMediaPlayer.reset();
        stop();
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        UpdatePlayerEvent event = new UpdatePlayerEvent(true, false);
        event.setDuration(mMediaPlayer.getDuration());
        BusManager.getInstance().getBus().post(event);
        progressChecker.run();
    }
}
