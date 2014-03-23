package com.prysmradio.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.events.PodcastsListEvent;
import com.squareup.otto.Subscribe;

/**
 * Created by fxoxe_000 on 09/03/14.
 */
public class PrysmFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radio, container, false);
    }


}
