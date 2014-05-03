package com.prysmradio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.adapters.MainPagerAdapter;
import com.prysmradio.bus.events.EpisodeEvent;
import com.prysmradio.bus.events.MediaPlayerErrorEvent;
import com.prysmradio.fragments.BottomPlayerFragment;
import com.prysmradio.objects.PodcastEpisode;
import com.prysmradio.utils.Constants;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends PrysmActivity implements ActionBar.TabListener {

    @InjectView(R.id.pager) ViewPager mainViewPager;

    private PodcastEpisode currentEpisode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mainViewPager.setOffscreenPageLimit(2);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(adapter);

        bottomPlayerFragment = (BottomPlayerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_bottom_player);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.tab_radio)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.tab_podcasts)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.tab_prysmradio)).setTabListener(this));

        mainViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSelectedNavigationItem(position);
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mainViewPager.setCurrentItem(tab.getPosition());

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // hide the given tab
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // probably ignore this event
    }

    @Override
    public void startStopAudioService() {
        if (!((PrysmApplication) getApplicationContext()).isServiceIsRunning()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
            Intent intent = new Intent(Constants.START_RADIO_SERVICE_ACTION);
            intent.putExtra(Constants.AUDIO_URL_EXTRA, getString(R.string.radio_url));
            startService(new Intent(intent));
            currentEpisode = null;
        } else {
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);
            startService(new Intent(Constants.STOP_RADIO_SERVICE_ACTION));
        }
    }



    @Subscribe
    public void onMediaPlayerErrorEventReceived(MediaPlayerErrorEvent event){
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_LONG).show();
    }

   @Subscribe
    public void onCurrentEpisodeReceived(EpisodeEvent event){
        currentEpisode = event.getEpisode();
    }

    @OnClick(R.id.player_layout)
    public void playerLayoutOnClick(View v){
        if (((PrysmApplication) getApplicationContext()).isServiceIsRunning()) {
            Intent podcastIntent = new Intent(this, PlayerActivity.class);
            podcastIntent.putExtra(Constants.EPISODE_EXTRA, currentEpisode);
            podcastIntent.putExtra(Constants.STREAM_TITLE_EXTRA, bottomPlayerFragment.getStreamTitle());
            startActivity(podcastIntent);
        }
    }
}
