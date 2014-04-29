package com.prysmradio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.prysmradio.R;
import com.prysmradio.api.ApiManager;
import com.prysmradio.api.requests.TrackHistoryRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.TrackHistoryEvent;
import com.prysmradio.bus.events.UpdateCoverEvent;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 09/03/14.
 */
public class RadioFragment extends PrysmFragment {

    @InjectView(R.id.now_playing_imageView) ImageView coverImageView;
    @InjectView(R.id.now_playing_artist_textView) TextView nowPlayingArtistTextView;
    @InjectView(R.id.now_playing_title_textView) TextView nowPlayingTitleTextView;
    @InjectView(R.id.track_history_progressBar) ProgressBar trackHistoryProgressBar;
    @InjectView(R.id.last_played_artist_1) TextView lastArtistPlayed1;
    @InjectView(R.id.last_played_artist_2) TextView lastArtistPlayed2;
    @InjectView(R.id.last_played_artist_3) TextView lastArtistPlayed3;
    @InjectView(R.id.last_played_title_1) TextView lastTitlePlayed1;
    @InjectView(R.id.last_played_title_2) TextView lastTitlePlayed2;
    @InjectView(R.id.last_played_title_3) TextView lastTitlePlayed3;
    @InjectView(R.id.last_played_layout) ViewGroup lastPlayedLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_radio, container, false);

        ButterKnife.inject(this, v);

        BusManager.getInstance().getBus().register(this);

        ApiManager.getInstance().invoke(null, new TrackHistoryRequest());

        return v;
    }

    @Subscribe
    public void onTrackHistoryEventReceived(TrackHistoryEvent event){
        trackHistoryProgressBar.setVisibility(View.GONE);
        if (event.getTrackHistory() != null && event.getTrackHistory().size() > 0){

            lastPlayedLayout.setVisibility(View.VISIBLE);

            lastArtistPlayed1.setText(event.getTrackHistory().get(0).getArtist());
            lastTitlePlayed1.setText(event.getTrackHistory().get(0).getTitle());

            lastArtistPlayed2.setText(event.getTrackHistory().get(1).getArtist());
            lastTitlePlayed2.setText(event.getTrackHistory().get(1).getTitle());

            lastArtistPlayed3.setText(event.getTrackHistory().get(2).getArtist());
            lastTitlePlayed3.setText(event.getTrackHistory().get(2).getTitle());
        }
    }


    @Subscribe
    public void onUpdateCoverEventReceived(UpdateCoverEvent event){
        if (event.getCurrentTrackInfo() != null){
            ImageLoader.getInstance().displayImage(event.getCurrentTrackInfo().getCover().getCover600x600(), coverImageView);
            nowPlayingArtistTextView.setText(event.getCurrentTrackInfo().getArtist());
            nowPlayingTitleTextView.setText(event.getCurrentTrackInfo().getTitle());
        } else {
            coverImageView.setImageResource(R.drawable.prysm_logo);
            nowPlayingArtistTextView.setText("");
            nowPlayingTitleTextView.setText("");
        }
        ApiManager.getInstance().invoke(null, new TrackHistoryRequest());
    }
}
