package com.prysmradio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.adapters.MainPagerAdapter;
import com.prysmradio.events.CheckPlayingEvent;
import com.prysmradio.events.CheckPlayingReturnEvent;
import com.prysmradio.utils.Constants;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {



    @InjectView(R.id.pager) ViewPager mainViewPager;
    @InjectView(R.id.play_pause_button) ImageView playPauseImageView;
    @InjectView(R.id.mute_button) ImageView muteImageView;

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(adapter);

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

        ((PrysmApplication) getApplicationContext()).getBus().post(new CheckPlayingEvent(true));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((PrysmApplication) getApplicationContext()).getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((PrysmApplication) getApplicationContext()).getBus().unregister(this);
    }

    @Subscribe
    public void CheckPlayingReturnEvent(CheckPlayingReturnEvent event){
        if (event.isShouldReadReturn()){
            Log.v(getString(R.string.app_name), "Event received ! " + event.isShouldReadReturn());
        }
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
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // hide the given tab
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // probably ignore this event
    }

    @OnClick(R.id.play_pause_button)
    public void playPauseOnClick(View v){
        if (!isPlaying){
            startService(new Intent(Constants.START_SERVICE_ACTION));
            playPauseImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
            isPlaying = true;
        } else {
            startService(new Intent(Constants.STOP_SERVICE_ACTION));
            playPauseImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
            isPlaying = false;
        }
    }

}
