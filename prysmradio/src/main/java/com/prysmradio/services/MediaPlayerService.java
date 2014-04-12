package com.prysmradio.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
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
import com.prysmradio.bus.events.UpdateMetaDataEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.bus.events.UpdatePodcastTitleEvent;
import com.prysmradio.utils.Constants;
import com.spoledge.aacdecoder.MultiPlayer;
import com.spoledge.aacdecoder.PlayerCallback;


public class MediaPlayerService extends Service implements AudioManager.OnAudioFocusChangeListener, PlayerCallback {

    private String audioUrl;

    private MultiPlayer player;
    private Handler runOnUiThreadHandler;
    private AudioManager audioManager;
    private boolean shouldPlay;

    @Override
    public void onCreate() {

        TelephonyManager mgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if( mgr != null )
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        runOnUiThreadHandler = new Handler();

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

        BusManager.getInstance().getBus().unregister(this);

        TelephonyManager mgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if( mgr != null )
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        if (action.equals(Constants.START_SERVICE_ACTION)) {
            shouldPlay = true;
            if (audioUrl == null || !audioUrl.equals(intent.getStringExtra(Constants.AUDIO_URL_EXTRA))){

                audioUrl = intent.getStringExtra(Constants.AUDIO_URL_EXTRA);

                if (intent.hasExtra(Constants.PODCAST_TITLE_EXTRA)){
                    String podcastTitle = intent.getStringExtra(Constants.PODCAST_TITLE_EXTRA);
                    String episodeTitle = intent.getStringExtra(Constants.EPISODE_TITLE_EXTRA);
                    UpdatePodcastTitleEvent event = new UpdatePodcastTitleEvent(podcastTitle, episodeTitle);
                    BusManager.getInstance().getBus().post(event);
                }

                start();
            }


        } else if (action.equals(Constants.STOP_SERVICE_ACTION)) {
            shouldPlay = false;
            stop();
        }

        return START_NOT_STICKY;
    }

    private synchronized void start() {
        BusManager.getInstance().getBus().post(new UpdatePlayerEvent(false, true));
        startForeground(Constants.NOTIFICATION_ID, ((PrysmApplication) getApplicationContext()).getNotificationHandler().getNotification());

        if (player != null) {
            player.stop();
            player = null;
        }

        player = new MultiPlayer(this);

        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            player = null;
        } else {
            player.playAsync(audioUrl);
        }
    }

    private synchronized void stop() {

        if (player != null) {
            player.stop();
            player = null;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BusManager.getInstance().getBus().post(new UpdatePlayerEvent(false, false));
            }
        });

        stopForeground(true);
        audioManager.abandonAudioFocus(this);
        stopSelf();
    }


    @Override
    public void playerStarted() {
        if (!shouldPlay){
            stop();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BusManager.getInstance().getBus().post(new UpdatePlayerEvent(true, false));
                }
            });
        }
    }

    @Override
    public void playerPCMFeedBuffer(boolean b, int i, int i2) {

    }

    @Override
    public void playerStopped(int i) {
        Log.v("MICHEL", "STOP !");
    }

    @Override
    public void playerException(final Throwable throwable) {

    }

    @Override
    public void playerMetadata(String s, final String s2) {
        if ("StreamTitle".equals(s)){
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
