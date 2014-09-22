package com.prysmradio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.fragments.BottomPlayerFragment;
import com.prysmradio.fragments.EpisodeFragment;
import com.prysmradio.fragments.RadioFragment;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.CurrentStreamInfo;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fx.oxeda on 07/04/2014.
 */
public class PlayerActivity extends PrysmActivity {

    @InjectView(R.id.add_layout)
    FrameLayout addLayout;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ButterKnife.inject(this);

        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.bloc_add_id));
        adView.setAdSize(AdSize.BANNER);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                addLayout.setVisibility(View.VISIBLE);
            }
        });

        addLayout.addView(adView);

        // Initiez une demande générique.
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("CED04524DB3BDE8671E1806D8B3C2D87")
                .build();

        // Chargez l'objet adView avec la demande d'annonce.
        adView.loadAd(adRequest);

        setProgressBarIndeterminateVisibility(getIntent().getBooleanExtra(Constants.LOADING_EXTRA, false));

        FragmentManager fm = getSupportFragmentManager();

        bottomPlayerFragment = (BottomPlayerFragment) fm.findFragmentById(R.id.fragment_bottom_player);
        String streamTitle = getIntent().getStringExtra(Constants.STREAM_TITLE_EXTRA);
        String streamArtist = getIntent().getStringExtra(Constants.STREAM_ARTIST_EXTRA);
        if (!TextUtils.isEmpty(streamTitle)){
            bottomPlayerFragment.setStreamTitleTextView(streamTitle);
        }
        if (!TextUtils.isEmpty(streamArtist)){
            bottomPlayerFragment.setStreamArtistTextView(streamArtist);
        }

        Fragment fragment;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        if (((PrysmApplication) getApplication()).isServiceIsRunning()){
            returnIntent.putExtra(Constants.STREAM_TITLE_EXTRA, bottomPlayerFragment.getStreamTitle());
            returnIntent.putExtra(Constants.STREAM_ARTIST_EXTRA, bottomPlayerFragment.getStreamArtist());
            setResult(RESULT_OK, returnIntent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
