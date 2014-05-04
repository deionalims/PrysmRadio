package com.prysmradio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.prysmradio.R;
import com.prysmradio.fragments.BottomPlayerFragment;
import com.prysmradio.fragments.EpisodeFragment;
import com.prysmradio.fragments.RadioFragment;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.CurrentStreamInfo;

/**
 * Created by fx.oxeda on 07/04/2014.
 */
public class PlayerActivity extends PrysmActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setProgressBarIndeterminateVisibility(getIntent().getBooleanExtra(Constants.LOADING_EXTRA, false));

        FragmentManager fm = getSupportFragmentManager();

        bottomPlayerFragment = (BottomPlayerFragment) fm.findFragmentById(R.id.fragment_bottom_player);
        String streamTitle = getIntent().getStringExtra(Constants.STREAM_TITLE_EXTRA);
        if (streamTitle != null){
            bottomPlayerFragment.setStreamTitleTextView(streamTitle);
        }

        Fragment fragment = null;

        if (CurrentStreamInfo.getInstance().getPodcastEpisode() != null){
            fragment = new EpisodeFragment();
        } else {
            fragment = new RadioFragment();
        }

        if (fragment != null){
            fm.beginTransaction()
                    .add(R.id.player_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.STREAM_TITLE_EXTRA, bottomPlayerFragment.getStreamTitle());
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
