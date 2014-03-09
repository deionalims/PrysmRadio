package com.prysmradio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prysmradio.R;

/**
 * Created by fxoxe_000 on 09/03/14.
 */
public class RadioFragment extends PrysmFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radio, container, false);
    }
}
