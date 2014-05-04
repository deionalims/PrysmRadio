package com.prysmradio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import com.google.gson.Gson;
import com.octo.android.robospice.SpiceManager;
import com.prysmradio.PrysmApplication;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdateCoverEvent;
import com.prysmradio.bus.events.UpdateMetaDataEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.fragments.BottomPlayerFragment;
import com.prysmradio.services.PrysmRetrofitSpiceService;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.CurrentStreamInfo;
import com.squareup.otto.Subscribe;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class PrysmActivity extends ActionBarActivity {

    private SpiceManager spiceManager = new SpiceManager(PrysmRetrofitSpiceService.class);
    protected BottomPlayerFragment bottomPlayerFragment;

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }

    @Subscribe
    public void onUpdatePlayerEventReceived(UpdatePlayerEvent event){
        if (event.isBuffering()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
            setProgressBarIndeterminateVisibility(true);
        } else if (event.isPlaying()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
            setProgressBarIndeterminateVisibility(false);
        } else {
            setProgressBarIndeterminateVisibility(false);
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);
            BusManager.getInstance().getBus().post(new UpdateCoverEvent(null));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusManager.getInstance().getBus().register(this);
        bottomPlayerFragment.setPlayPauseImageView(((PrysmApplication) getApplicationContext()).isServiceIsRunning());
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusManager.getInstance().getBus().unregister(this);
    }

    public BottomPlayerFragment getBottomPlayerFragment() {
        return bottomPlayerFragment;
    }

    public void startStopAudioService() {
        if (!((PrysmApplication) getApplicationContext()).isServiceIsRunning()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);

            if (CurrentStreamInfo.getInstance().getCurrentRadio() != null){
                Intent intent = new Intent(Constants.START_RADIO_SERVICE_ACTION);
                intent.putExtra(Constants.AUDIO_URL_EXTRA, CurrentStreamInfo.getInstance().getCurrentRadio().getAACStreamURL());
                startService(new Intent(intent));
            } else if (CurrentStreamInfo.getInstance().getPodcastEpisode() != null){
                Intent intent = new Intent(Constants.START_PODCAST_SERVICE_ACTION);
                intent.putExtra(Constants.AUDIO_URL_EXTRA, CurrentStreamInfo.getInstance().getPodcastEpisode().getAudioUrl());
                BusManager.getInstance().getBus().post(new UpdateMetaDataEvent(CurrentStreamInfo.getInstance().getPodcastEpisode().getTitle()));
                startService(new Intent(intent));
            }
        } else {
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);
            if (CurrentStreamInfo.getInstance().getCurrentRadio() != null){
                startService(new Intent(Constants.STOP_RADIO_SERVICE_ACTION));
                saveCurrentRadio();
            } else if (CurrentStreamInfo.getInstance().getPodcastEpisode() != null){
                startService(new Intent(Constants.STOP_PODCAST_SERVICE_ACTION));
                saveCurrentPodcastEdisode();
            }
        }
    }

    private void saveCurrentRadio(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preferences.edit();

        edit.remove(Constants.PODCAST_EPISODE_PREF);
        Gson gson = new Gson();
        String json = gson.toJson(CurrentStreamInfo.getInstance().getCurrentRadio());
        edit.putString(Constants.RADIO_PREF, json);

        edit.commit();
    }

    private void saveCurrentPodcastEdisode(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preferences.edit();

        edit.remove(Constants.RADIO_PREF);
        Gson gson = new Gson();
        String json = gson.toJson(CurrentStreamInfo.getInstance().getPodcastEpisode());
        edit.putString(Constants.PODCAST_EPISODE_PREF, json);

        edit.commit();
    }
}
