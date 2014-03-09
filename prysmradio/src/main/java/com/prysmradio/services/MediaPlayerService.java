package com.prysmradio.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import com.prysmradio.R;
import com.prysmradio.utils.Constants;

import java.io.IOException;


public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {

    private MediaPlayer mMediaPlayer = null;

    @Override
    public void onCreate() {
        mMediaPlayer = new MediaPlayer();

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onDestroy() {
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        if (action.equals(Constants.START_SERVICE_ACTION)) {
            start();
        } else if (action.equals(Constants.STOP_SERVICE_ACTION)){
            stop();
        }

        return START_NOT_STICKY;
    }

    public synchronized void start(){
        try {
            mMediaPlayer.setDataSource(this, Uri.parse(getString(R.string.radio_url))); // Go to Initialized state
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        } catch (IOException e){
            mMediaPlayer.reset();
        }
    }

    public synchronized void stop(){
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }

        mMediaPlayer.reset();
        // TODO Stop foreground

        stopSelf();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mMediaPlayer.reset();
        } else {
            mMediaPlayer.start();
            //TODO Start fore ground
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }
}
