package com.prysmradio.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.fragments.BottomPlayerFragment;
import com.prysmradio.fragments.RadiosListFragment;
import com.prysmradio.utils.Constants;
import com.prysmradio.views.DrawerView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends PrysmActivity implements DrawerLayout.DrawerListener {

    @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @InjectView(R.id.left_drawer) DrawerView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerToggle.syncState();

        mDrawerLayout.setDrawerListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        bottomPlayerFragment = (BottomPlayerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_bottom_player);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(getString(R.string.pref_auto_play), false)){
            startStopAudioService();
        }

        replaceFragment(new RadiosListFragment(), false, null);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mDrawerToggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerToggle.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        mDrawerToggle.onDrawerStateChanged(newState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        return intent;
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack, String tag){
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment);
        if (addToBackStack){
            ft.addToBackStack(tag);
        }

        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.player_layout)
    public void playerLayoutOnClick(View v){
        if (((PrysmApplication) getApplicationContext()).isServiceIsRunning()) {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra(Constants.STREAM_TITLE_EXTRA, bottomPlayerFragment.getStreamTitle());
            intent.putExtra(Constants.STREAM_ARTIST_EXTRA, bottomPlayerFragment.getStreamArtist());
            startActivityForResult(intent, Constants.METADATA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.METADATA_REQUEST_CODE && resultCode == RESULT_OK){
            String streamTitle = data.getStringExtra(Constants.STREAM_TITLE_EXTRA);
            bottomPlayerFragment.setStreamTitleTextView(streamTitle);
            String streamArtist = data.getStringExtra(Constants.STREAM_ARTIST_EXTRA);
            bottomPlayerFragment.setStreamArtistTextView(streamArtist);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle data = intent.getExtras();
        if (data != null && data.getBoolean(Constants.STOP_EXTRA, false)) {
            startStopAudioService();
        }
    }
}
