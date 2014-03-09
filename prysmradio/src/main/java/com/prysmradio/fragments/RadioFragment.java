package com.prysmradio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.prysmradio.R;
import com.prysmradio.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by fxoxe_000 on 09/03/14.
 */
public class RadioFragment extends PrysmFragment {

    @InjectView(R.id.play_pause_button)
    ImageView playPauseButton;

    private boolean isPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_radio, container, false);

        ButterKnife.inject(this, v);

        return v;
    }

    @OnClick(R.id.play_pause_button)
    public void playPauseOnClick(View v){
        if (!isPlaying){
            getActivity().startService(new Intent(Constants.START_SERVICE_ACTION));
            playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
            isPlaying = true;
        } else {
            getActivity().startService(new Intent(Constants.STOP_SERVICE_ACTION));
            playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
            isPlaying = false;
        }

    }
}
