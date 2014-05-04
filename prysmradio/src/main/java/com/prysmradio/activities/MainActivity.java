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

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.adapters.MainPagerAdapter;
import com.prysmradio.fragments.BottomPlayerFragment;
import com.prysmradio.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends PrysmActivity implements ActionBar.TabListener {

    @InjectView(R.id.pager) ViewPager mainViewPager;

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

    @OnClick(R.id.player_layout)
    public void playerLayoutOnClick(View v){
        if (((PrysmApplication) getApplicationContext()).isServiceIsRunning()) {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra(Constants.STREAM_TITLE_EXTRA, bottomPlayerFragment.getStreamTitle());
            startActivityForResult(intent, Constants.METADATA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.METADATA_REQUEST_CODE && resultCode == RESULT_OK){
            String streamTitle = data.getStringExtra(Constants.STREAM_TITLE_EXTRA);
            bottomPlayerFragment.setStreamTitleTextView(streamTitle);
        }
    }
}
