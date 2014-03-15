package com.prysmradio.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.events.CheckPlayingEvent;
import com.prysmradio.events.CheckPlayingReturnEvent;
import com.prysmradio.utils.Constants;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;


public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {

    private MediaPlayer mMediaPlayer = null;
    private Bus bus;

    @Override
    public void onCreate() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);

        bus = new Bus();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        bus.unregister(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        if (action.equals(Constants.START_SERVICE_ACTION)) {
            init();
        } else if (action.equals(Constants.STOP_SERVICE_ACTION)) {
            stop();
        }

        return START_NOT_STICKY;
    }

    private synchronized void init() {
        try {
            mMediaPlayer.setDataSource(this, Uri.parse(getString(R.string.radio_url))); // Go to Initialized state
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        } catch (IOException e) {
            mMediaPlayer.reset();
        }
    }

    private synchronized void start() {
        mMediaPlayer.start();
        startForeground(Constants.NOTIFICATION_ID, ((PrysmApplication) getApplicationContext()).getNotificationHandler().getNotification());
    }

    private synchronized void stop() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }

        resetPlayer();
        stopForeground(true);

        stopSelf();
    }

    private synchronized void resetPlayer() {
        mMediaPlayer.reset();
        mMediaPlayer = null;
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
            start();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mMediaPlayer == null) {
                    init();
                } else if (!mMediaPlayer.isPlaying()) {
                    start();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                resetPlayer();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
                break;

        }
    }

    @Subscribe
    public void onCheckPlayingEvent(CheckPlayingEvent event){
        if (event.isShouldCheckPlaying()){
            Log.v(getString(R.string.app_name), "Checking playback !");
            bus.post(new CheckPlayingReturnEvent(true));
        }
    }

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {

        private boolean mWasPlayingWhenCalled = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) // Incoming call: Pause music
            {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mWasPlayingWhenCalled = true;
                }
            } else if (state == TelephonyManager.CALL_STATE_IDLE) // Not in call: Play music
            {
                if (mWasPlayingWhenCalled) {
                    start();
                    mWasPlayingWhenCalled = false;
                }
            }

            super.onCallStateChanged(state, incomingNumber);
        }
    };
}
