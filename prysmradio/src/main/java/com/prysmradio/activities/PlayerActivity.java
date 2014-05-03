package com.prysmradio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.fragments.BottomPlayerFragment;
import com.prysmradio.fragments.RadioFragment;
import com.prysmradio.objects.PodcastEpisode;
import com.prysmradio.utils.Constants;

/**
 * Created by fx.oxeda on 07/04/2014.
 */
public class PlayerActivity extends PrysmActivity {

    private PodcastEpisode episode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setProgressBarIndeterminateVisibility(getIntent().getBooleanExtra(Constants.LOADING_EXTRA, false));
        episode = (PodcastEpisode)getIntent().getSerializableExtra(Constants.EPISODE_EXTRA);

        FragmentManager fm = getSupportFragmentManager();

        bottomPlayerFragment = (BottomPlayerFragment) fm.findFragmentById(R.id.fragment_bottom_player);
        String streamTitle = getIntent().getStringExtra(Constants.STREAM_TITLE_EXTRA);
        if (streamTitle != null){
            bottomPlayerFragment.setStreamTitleTextView(streamTitle);
        }

        if (episode != null){

        } else {
            RadioFragment radioFragment = new RadioFragment();
            fm.beginTransaction()
                    .add(R.id.player_container, radioFragment)
                    .commit();
        }
    }

    @Override
    public void startStopAudioService() {
        if (!((PrysmApplication) getApplicationContext()).isServiceIsRunning()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
            Intent intent = new Intent(Constants.START_RADIO_SERVICE_ACTION);
            intent.putExtra(Constants.AUDIO_URL_EXTRA, getString(R.string.radio_url));
            startService(new Intent(intent));
        } else {
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);
            startService(new Intent(Constants.STOP_RADIO_SERVICE_ACTION));
        }
    }
}
