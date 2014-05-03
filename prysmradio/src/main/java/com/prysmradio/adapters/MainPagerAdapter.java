package com.prysmradio.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.prysmradio.fragments.NewsListFragment;
import com.prysmradio.fragments.PodcastsFragment;
import com.prysmradio.fragments.RadiosListFragment;

/**
 * Created by fxoxe_000 on 09/03/14.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {


    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new RadiosListFragment();
            case 1:
                return new PodcastsFragment();
            case 2:
                return new NewsListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
