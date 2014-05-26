package com.prysmradio.services;

import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.SendCurrentTrackInfoRequestEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.utils.Constants;
import com.spoledge.aacdecoder.MultiPlayer;
import com.spoledge.aacdecoder.PlayerCallback;


public class RadioPlayerService extends PrysmAudioService implements PlayerCallback {

    private static final String METADATA_STREAM_TITLE_KEY = "StreamTitle";

    private MultiPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();

        player = new MultiPlayer(this);

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
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceID = startId;
        String action = intent.getAction();
        ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);

        if (action.equals(Constants.START_RADIO_SERVICE_ACTION)) {

            if (audioUrl == null || !audioUrl.equals(intent.getStringExtra(Constants.AUDIO_URL_EXTRA))){

                audioUrl = intent.getStringExtra(Constants.AUDIO_URL_EXTRA);

                if (state == STATE.PLAYING){
                    state = STATE.SHOULD_LOAD_URL;
                    player.stop();
                } else {
                    start();
                }
            }
        } else if (action.equals(Constants.STOP_RADIO_SERVICE_ACTION)) {

            if (state == STATE.PREPARING){
                state = STATE.SHOULD_QUIT;
            } else {
                stop();
            }
        }
        return START_NOT_STICKY;
    }

    protected synchronized void start() {
        super.start();

        startForeground(Constants.NOTIFICATION_ID, ((PrysmApplication) getApplicationContext()).getNotificationHandler().getNotification());

        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                player.playAsync(audioUrl);
        }
    }

    protected synchronized void stop(){
        state = STATE.STOPPED;
        player.stop();
        super.stop();
    }

    @Override
    void pauseMusic() {
        state = STATE.STOPPED;
        player.stop();
    }

    @Override
    void resumeMusic() {
        player.playAsync(audioUrl);
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
    public void playerPCMFeedBuffer(boolean b, int audioBufferSizeMs, int audioBufferCapacityMs) {

    }

    @Override
    public void playerStopped(int i){
        if (state == STATE.SHOULD_LOAD_URL){
            start();
        } else if (state == STATE.PLAYING){
            player.playAsync(audioUrl);
        } else {
            stop();
        }
    }

    @Override
    public void playerException(final Throwable throwable) {
        Log.v("MICHEL", "Player Exception : " + throwable.getMessage());
    }

    @Override
    public void playerMetadata(String s, final String s2) {
        if (METADATA_STREAM_TITLE_KEY.equals(s) && state == STATE.PLAYING){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BusManager.getInstance().getBus().post(new SendCurrentTrackInfoRequestEvent(true));
                }
            });
        }
    }

    @Override
    public void playerAudioTrackCreated(AudioTrack audioTrack) {

    }




}
