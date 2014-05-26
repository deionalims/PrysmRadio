package com.prysmradio.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.prysmradio.PrysmApplication;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.utils.Constants;

/**
 * Created by fxoxe_000 on 21/04/2014.
 */
public abstract class PrysmAudioService extends Service implements AudioManager.OnAudioFocusChangeListener {

    public enum STATE {
        PLAYING, PREPARING, SHOULD_LOAD_URL, SHOULD_QUIT, STOPPED
    }

    protected String audioUrl;
    protected AudioManager audioManager;
    protected int serviceID;
    protected STATE state;
    protected Handler runOnUiThreadHandler;
    protected int currentVolume;

    @Override
    public void onCreate() {
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null)
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        runOnUiThreadHandler = new Handler();

//        registerReceiver(
//        new ConnectivityChangeReceiver(),
//        new IntentFilter(
//                ConnectivityManager.CONNECTIVITY_ACTION));


        BusManager.getInstance().getBus().register(this);
    }

    @Override
    public void onDestroy() {
        ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);

        BusManager.getInstance().getBus().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected synchronized void start() {
        state = STATE.PREPARING;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BusManager.getInstance().getBus().post(new UpdatePlayerEvent(false, true));
            }
        });

        startForeground(Constants.NOTIFICATION_ID, ((PrysmApplication) getApplicationContext()).getNotificationHandler().getNotification());
    }

    abstract void pauseMusic();
    abstract void resumeMusic();


    protected synchronized void stop() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BusManager.getInstance().getBus().post(new UpdatePlayerEvent(false, false));
            }
        });

        stopForeground(true);
        audioManager.abandonAudioFocus(this);
        Log.v("MICHEL", "STOPPING " + serviceID);
        stopSelf(serviceID);
    }

    protected void runOnUiThread(Runnable runnable) {
        runOnUiThreadHandler.post(runnable);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (state == STATE.PLAYING){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
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
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (state == STATE.PLAYING){
                    currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 2, 0);
                }

                break;
        }
    }

    protected PhoneStateListener phoneStateListener = new PhoneStateListener() {

        private boolean mWasPlayingWhenCalled = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (state == TelephonyManager.CALL_STATE_RINGING) // Incoming call: Pause music
            {
                mWasPlayingWhenCalled = true;
                pauseMusic();
            } else if (state == TelephonyManager.CALL_STATE_IDLE) // Not in call: Play music
            {
                if (mWasPlayingWhenCalled) {
                    resumeMusic();
                    mWasPlayingWhenCalled = false;
                }
            }

            super.onCallStateChanged(state, incomingNumber);
        }
    };

    private class ConnectivityChangeReceiver
            extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            debugIntent(intent, "MICHEL");
        }

        private void debugIntent(Intent intent, String tag) {
            Log.v(tag, "action: " + intent.getAction());
            Log.v(tag, "component: " + intent.getComponent());
            Bundle extras = intent.getExtras();
            if (extras != null) {
                for (String key: extras.keySet()) {
                    Log.v(tag, "key [" + key + "]: " +
                            extras.get(key));
                }
            }
            else {
                Log.v(tag, "no extras");
            }
        }

    }
}
