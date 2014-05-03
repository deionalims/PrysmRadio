package com.prysmradio.activities;

import android.support.v7.app.ActionBarActivity;

import com.octo.android.robospice.SpiceManager;
import com.prysmradio.PrysmApplication;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdateCoverEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.fragments.BottomPlayerFragment;
import com.prysmradio.services.PrysmRetrofitSpiceService;
import com.squareup.otto.Subscribe;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public abstract class PrysmActivity extends ActionBarActivity {

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

    abstract public void startStopAudioService();
}
