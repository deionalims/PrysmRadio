package com.prysmradio.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.api.ApiManager;
import com.prysmradio.api.requests.CurrentTrackInfoRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdateEpisodeProgressEvent;
import com.prysmradio.bus.events.UpdateMetaDataEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.bus.events.UpdatePodcastTitleEvent;
import com.prysmradio.utils.Constants;
import com.spoledge.aacdecoder.MultiPlayer;
import com.spoledge.aacdecoder.PlayerCallback;

import java.io.IOException;


public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener, PlayerCallback, MediaPlayer.OnSeekCompleteListener {

    public enum STATE {
        PLAYING, PREPARING, SHOULD_LOAD_URL, SHOULD_QUIT, STOPPED
    }

    private String audioUrl;
    private MultiPlayer player;
    private MediaPlayer mMediaPlayer = null;
    private Handler runOnUiThreadHandler;
    private AudioManager audioManager;
    private int serviceID;
    private STATE state;
    private Handler progressHandler;
    private Runnable progressChecker;


    @Override
    public void onCreate() {
        TelephonyManager mgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if( mgr != null )
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setVolume(0,5f);

        player = new MultiPlayer(this);

        runOnUiThreadHandler = new Handler();
        progressHandler = new Handler();
        progressChecker = new Runnable() {
            @Override
            public void run() {
                retrieveProgress();
                progressHandler.postDelayed(progressChecker, 1000);
            }
        };

        try {
            java.net.URL.setURLStreamHandlerFactory( new java.net.URLStreamHandlerFactory(){
                public java.net.URLStreamHandler createURLStreamHandler( String protocol ) {
                    Log.d(getString(R.string.app_name), "Asking for stream handler for protocol: '" + protocol + "'");
                    if ("icy".equals( protocol )) return new com.spoledge.aacdecoder.IcyURLStreamHandler();
                    return null;
                }
            });
        }
        catch (Throwable t) {
            Log.e(getString(R.string.app_name), "Cannot set the ICY URLStreamHandler - maybe already set ? - " + t );
        }

        BusManager.getInstance().getBus().register(this);
    }

    @Override
    public void onDestroy() {
        ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);
        mMediaPlayer.release();

        BusManager.getInstance().getBus().unregister(this);

        TelephonyManager mgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if( mgr != null )
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceID = startId;
        String action = intent.getAction();
        ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
        progressHandler.removeCallbacks(progressChecker);
        if (action.equals(Constants.START_SERVICE_ACTION)) {

            if (audioUrl == null || !audioUrl.equals(intent.getStringExtra(Constants.AUDIO_URL_EXTRA))){

                audioUrl = intent.getStringExtra(Constants.AUDIO_URL_EXTRA);

                if (intent.hasExtra(Constants.PODCAST_TITLE_EXTRA)){
                    String podcastTitle = intent.getStringExtra(Constants.PODCAST_TITLE_EXTRA);
                    String episodeTitle = intent.getStringExtra(Constants.EPISODE_TITLE_EXTRA);
                    UpdatePodcastTitleEvent event = new UpdatePodcastTitleEvent(podcastTitle, episodeTitle);
                    BusManager.getInstance().getBus().post(event);
                }

                if (state == STATE.PLAYING || state == STATE.PREPARING || state == STATE.SHOULD_LOAD_URL) {
                    if (state == STATE.PLAYING){
                        if (mMediaPlayer.isPlaying()){
                            mMediaPlayer.stop();
                            mMediaPlayer.reset();
                            start();
                        } else {
                            state = STATE.SHOULD_LOAD_URL;
                            player.stop();
                        }
                    } else if (state == STATE.PREPARING) {
                        state = STATE.SHOULD_LOAD_URL;
                    }
                } else {
                    start();
                }
            }
        } else if (action.equals(Constants.STOP_SERVICE_ACTION)) {
            if (state == STATE.PLAYING || state == STATE.PREPARING){
                if (state == STATE.PLAYING) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.reset();
                        stop();
                    } else {
                        state = STATE.SHOULD_QUIT;
                        player.stop();
                    }
                } else {
                    state = STATE.SHOULD_QUIT;
                }
            } else {
                stop();
            }
        } else if (action.equals(Constants.SEEK_SERVICE_ACTION)){
            int seekTo = intent.getIntExtra(Constants.SEEK_TO_EXTRA, -1);
            if (state == STATE.PLAYING && mMediaPlayer.isPlaying() && seekTo != -1){
                mMediaPlayer.seekTo(seekTo);
                BusManager.getInstance().getBus().post(new UpdatePlayerEvent(false, true));
            }
        }

        return START_NOT_STICKY;
    }

    private synchronized void start() {
        state = STATE.PREPARING;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BusManager.getInstance().getBus().post(new UpdatePlayerEvent(false, true));
            }
        });

        startForeground(Constants.NOTIFICATION_ID, ((PrysmApplication) getApplicationContext()).getNotificationHandler().getNotification());

        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            if (audioUrl.endsWith(".mp3")){
                try {
                    mMediaPlayer.setDataSource(audioUrl);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e){
                    Log.e(getPackageName(), e.getMessage());
                }
            } else {
                player.playAsync(audioUrl);
            }
        }
    }

    private synchronized void stop() {
        progressHandler.removeCallbacks(progressChecker);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BusManager.getInstance().getBus().post(new UpdatePlayerEvent(false, false));
            }
        });

        stopForeground(true);
        audioManager.abandonAudioFocus(this);
        stopSelf(serviceID);
    }


    @Override
    public void playerStarted() {
        if (state == STATE.SHOULD_QUIT || state == STATE.SHOULD_LOAD_URL){
            player.stop();
            return;
        }

        state = STATE.PLAYING;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BusManager.getInstance().getBus().post(new UpdatePlayerEvent(true, false));
            }
        });

    }

    @Override
    public void playerPCMFeedBuffer(boolean b, int i, int i2) {


    }

    @Override
    public void playerStopped(int i){
        if (state == STATE.SHOULD_LOAD_URL){
            start();
        } else {
            stop();
        }
    }

    @Override
    public void playerException(final Throwable throwable) {

    }

    @Override
    public void playerMetadata(String s, final String s2) {
        if ("StreamTitle".equals(s) && state == STATE.PLAYING){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UpdateMetaDataEvent event = new UpdateMetaDataEvent(s2);
                    BusManager.getInstance().getBus().post(event);
                    ApiManager.getInstance().invoke(null, new CurrentTrackInfoRequest());
                }
            });

        }
    }

    @Override
    public void playerAudioTrackCreated(AudioTrack audioTrack) {

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                start();
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                stop();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                stop();
                break;

        }
    }

    private void runOnUiThread(Runnable runnable) {
        runOnUiThreadHandler.post(runnable);
    }

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {

        private boolean mWasPlayingWhenCalled = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) // Incoming call: Pause music
            {

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
