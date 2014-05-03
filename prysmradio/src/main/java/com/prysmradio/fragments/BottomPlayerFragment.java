package com.prysmradio.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prysmradio.R;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdateCoverEvent;
import com.prysmradio.bus.events.UpdateMetaDataEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.bus.events.UpdatePodcastTitleEvent;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class BottomPlayerFragment extends PrysmFragment {

    @InjectView(R.id.play_pause_button)
    ImageView playPauseImageView;
    @InjectView(R.id.stream_title_textView)
    TextView streamTitleTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bottom_player, container, false);

        ButterKnife.inject(this, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusManager.getInstance().getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusManager.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void onUpdateMetaDataEventReceived(UpdateMetaDataEvent event){
        if (!TextUtils.isEmpty(event.getStreamTitle())){
            streamTitleTextView.setText(event.getStreamTitle());
        }
    }

    public void setPlayPauseImageView(boolean isPlaying){

        playPauseImageView.setImageDrawable(
                        isPlaying ?
                        getResources().getDrawable(R.drawable.ic_action_stop) :
                        getResources().getDrawable(R.drawable.ic_action_play));

        if (!isPlaying){
            streamTitleTextView.setText("");
        }
    }

    @Subscribe
    public void onUpdatePlayerEventReceived(UpdatePlayerEvent event){
        if (event.isBuffering()){
            playPauseImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
        } else if (event.isPlaying()){
            playPauseImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
        } else {
            playPauseImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
            streamTitleTextView.setText("");
            BusManager.getInstance().getBus().post(new UpdateCoverEvent(null));
        }
    }

    @OnClick(R.id.play_pause_button)
    public void playPauseOnClick(View v){
        getPrysmActivity().startStopAudioService();
    }


    @Subscribe
    public void onUpdatePodcastTitleEventReceived(UpdatePodcastTitleEvent event){
        if (!TextUtils.isEmpty(event.getPodcastTitle())){
            streamTitleTextView.setText(event.getPodcastTitle());
        }
    }

    public void setStreamTitleTextView(String streamTitle) {
        this.streamTitleTextView.setText(streamTitle);
    }

    public String getStreamTitle(){
        return streamTitleTextView.getText().toString();
    }
}
