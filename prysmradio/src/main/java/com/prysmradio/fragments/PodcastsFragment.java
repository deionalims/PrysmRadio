package com.prysmradio.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prysmradio.R;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class PodcastsFragment extends PrysmFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_podcasts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getActivity().getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.container, new PodcastsListFragment())
                .commit();
    }


}
