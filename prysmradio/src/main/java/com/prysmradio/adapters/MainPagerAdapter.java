package com.prysmradio.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.prysmradio.fragments.PrysmFragment;
import com.prysmradio.fragments.RadioFragment;

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
                return new RadioFragment();
            case 1:
                return new PrysmFragment();
            case 2:
                return new PrysmFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
