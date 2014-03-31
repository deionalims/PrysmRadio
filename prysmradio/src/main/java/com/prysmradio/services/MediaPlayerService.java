package com.prysmradio.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.MediaPlayerErrorEvent;
import com.prysmradio.bus.events.UpdateMetaDataEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.bus.events.UpdatePodcastTitleEvent;
import com.prysmradio.utils.BackgroundExecutor;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.IcyStreamMeta;

import java.io.IOException;
import java.net.URL;


public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {

    private enum MediaPlayerState {
        IDLE,
        INITIALIZED,
        PREPARING,
        PREPARED,
        STARTED,
        PAUSED,
        STOPPED,
        END
    }

    private String audioUrl;

    private MediaPlayer mMediaPlayer = null;
    private Handler metadataHandler;
    private Runnable metadataChecker;
    private AudioManager audioManager;

    private MediaPlayerState state;
    private IcyStreamMeta icyStreamMeta;

    @Override
    public void onCreate() {

        metadataHandler = new Handler();
        metadataChecker = new Runnable() {
            @Override
            public void run() {
                retrieveMetaData();
                metadataHandler.postDelayed(metadataChecker, 10000);
            }
        };

        icyStreamMeta = new IcyStreamMeta();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);

        TelephonyManager mgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if( mgr != null )
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        state = MediaPlayerState.IDLE;

        BusManager.getInstance().getBus().register(this);
    }

    @Override
    public void onDestroy() {

        BusManager.getInstance().getBus().unregister(this);

        TelephonyManager mgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if( mgr != null )
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);

        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        audioUrl = intent.getStringExtra(Constants.AUDIO_URL_EXTRA);

        if (action.equals(Constants.START_SERVICE_ACTION)) {
            init(intent);
        } else if (action.equals(Constants.STOP_SERVICE_ACTION)) {
            stop();
        }

        return START_NOT_STICKY;
    }

    private synchronized void init(Intent intent) {

        if (state == MediaPlayerState.PREPARING){
            BusManager.getInstance().getBus().post(new MediaPlayerErrorEvent(getString(R.string.media_player_is_busy)));
            return;
        }

        try {
            BusManager.getInstance().getBus().post(new UpdatePlayerEvent(false, true));
            if (state == MediaPlayerState.STARTED || state == MediaPlayerState.PAUSED){
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            mMediaPlayer.setDataSource(this, Uri.parse(audioUrl)); // Go to Initialized state
            state = MediaPlayerState.INITIALIZED;
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
            state = MediaPlayerState.PREPARING;

            if (intent.hasExtra(Constants.PODCAST_TITLE_EXTRA)){
                metadataHandler.removeCallbacks(metadataChecker);
                String podcastTitle = intent.getStringExtra(Constants.PODCAST_TITLE_EXTRA);
                String episodeTitle = intent.getStringExtra(Constants.EPISODE_TITLE_EXTRA);
                UpdatePodcastTitleEvent event = new UpdatePodcastTitleEvent(podcastTitle, episodeTitle);
                BusManager.getInstance().getBus().post(event);
            } else {
                icyStreamMeta.setStreamUrl(new URL(getString(R.string.radio_url)));
                metadataChecker.run();
            }
        } catch (IOException e) {
            resetPlayer();
        }
    }

    private void retrieveMetaData(){
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    icyStreamMeta.refreshMeta();
                } catch (IOException e){
                    Log.e(getString(R.string.app_name), e.getMessage());
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                try {
                    if (!TextUtils.isEmpty(icyStreamMeta.getStreamTitle())){
                        UpdateMetaDataEvent event = new UpdateMetaDataEvent(icyStreamMeta.getArtist(), icyStreamMeta.getTitle());
                        BusManager.getInstance().getBus().post(event);
                    }
                } catch (IOException e){
                    Log.e(getString(R.string.app_name), e.getMessage());
                }

            }
        });
    }

    private synchronized void start() {
        mMediaPlayer.start();
        state = MediaPlayerState.STARTED;
        BusManager.getInstance().getBus().post(new UpdatePlayerEvent(true, false));
        startForeground(Constants.NOTIFICATION_ID, ((PrysmApplication) getApplicationContext()).getNotificationHandler().getNotification());
    }

    private synchronized void stop() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }

        state = MediaPlayerState.STOPPED;
        stopForeground(true);
        audioManager.abandonAudioFocus(this);
        resetPlayer();
        stopSelf();
    }

    private synchronized void resetPlayer() {
        mMediaPlayer.release();
        mMediaPlayer = null;
        BusManager.getInstance().getBus().post(new UpdatePlayerEvent(false, false));
        metadataHandler.removeCallbacks(metadataChecker);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        state = MediaPlayerState.PREPARED;

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
                if (!mMediaPlayer.isPlaying()) {
                    start();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                stop();
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
