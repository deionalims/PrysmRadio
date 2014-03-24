package com.prysmradio.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.adapters.MainPagerAdapter;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdateMetaDataEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.services.MediaPlayerService;
import com.prysmradio.utils.Constants;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {



    @InjectView(R.id.pager) ViewPager mainViewPager;
    @InjectView(R.id.play_pause_button) ImageView playPauseImageView;
    @InjectView(R.id.mute_button) ImageView muteImageView;
    @InjectView(R.id.artist_textView)
    TextView artistTextView;
    @InjectView(R.id.title_textView) TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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

        playPauseImageView.setImageDrawable(
                ((PrysmApplication) getApplicationContext()).isServiceIsRunning() ?
                        getResources().getDrawable(R.drawable.ic_action_stop) :
                        getResources().getDrawable(R.drawable.ic_action_play));
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusManager.getInstance().getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusManager.getInstance().getBus().unregister(this);
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

    @OnClick(R.id.play_pause_button)
    public void playPauseOnClick(View v){
        if (!((PrysmApplication) getApplicationContext()).isServiceIsRunning()){
            startService(new Intent(Constants.START_SERVICE_ACTION));
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
        } else {
            startService(new Intent(Constants.STOP_SERVICE_ACTION));
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);
        }
    }

    @Subscribe
    public void onUpdatePlayerEventReceived(UpdatePlayerEvent event){

        if (event.isBuffering()){
            setProgressBarIndeterminateVisibility(true);
            playPauseImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
        } else if (event.isPlaying()){
            setProgressBarIndeterminateVisibility(false);
        } else {
            playPauseImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
            artistTextView.setText("");
            titleTextView.setText("");
        }
    }

    @Subscribe
    public void onUpdateMetaDataEventReceived(UpdateMetaDataEvent event){
        if (!TextUtils.isEmpty(event.getArtist())){
            artistTextView.setText(String.format(getString(R.string.artist), event.getArtist()));
        }
        if (!TextUtils.isEmpty(event.getTitle())){
            titleTextView.setText(String.format(getString(R.string.title), event.getTitle()));
        }
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MediaPlayerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
