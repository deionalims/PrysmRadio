package com.prysmradio.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import com.prysmradio.R;

import java.io.IOException;

/**
 * Created by fxoxe_000 on 09/03/14.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    MediaPlayer mMediaPlayer = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {



        try {
            mMediaPlayer.setDataSource(this, Uri.parse(getString(R.string.radio_url))); // Go to Initialized state
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        } catch (IOException e){

        }


        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
