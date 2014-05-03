package com.prysmradio.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.R;
import com.prysmradio.api.requests.CurrentTrackInfoRequest;
import com.prysmradio.api.requests.TrackHistoryRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdateMetaDataEvent;
import com.prysmradio.objects.CurrentTrackInfo;
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

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusManager.getInstance().getBus().register(this);
        TrackHistoryRequest request = new TrackHistoryRequest(1);
        getSpiceManager().execute(request, TrackHistoryRequest.TRACK_HISTORY_REQUEST, DurationInMillis.ONE_MINUTE, new TrackHistoryListener());

        CurrentTrackInfoRequest currentTrackInfoRequest = new CurrentTrackInfoRequest(1);
        getSpiceManager().execute(currentTrackInfoRequest, CurrentTrackInfoRequest.CURRENT_TRACK_REQUEST, DurationInMillis.ONE_MINUTE, new CurrentTrackListener());
    }

    @Override
    public void onPause() {
        super.onPause();
        BusManager.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void onUpdateMetaDataEventReceived(UpdateMetaDataEvent event){
        if (!TextUtils.isEmpty(event.getStreamTitle())){
            CurrentTrackInfoRequest request = new CurrentTrackInfoRequest(1);
            SpiceManager manager = getSpiceManager();
            if (manager != null){
                manager.execute(request, CurrentTrackInfoRequest.CURRENT_TRACK_REQUEST, DurationInMillis.ONE_MINUTE, new CurrentTrackListener());
            }
        }
    }

    private class TrackHistoryListener implements RequestListener<CurrentTrackInfo.List> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showError(spiceException.getMessage());
        }

        @Override
        public void onRequestSuccess(CurrentTrackInfo.List currentTrackInfos) {
            trackHistoryProgressBar.setVisibility(View.GONE);
            if (currentTrackInfos != null && currentTrackInfos.size() > 0){

                lastPlayedLayout.setVisibility(View.VISIBLE);

                lastArtistPlayed1.setText(currentTrackInfos.get(0).getArtist());
                lastTitlePlayed1.setText(currentTrackInfos.get(0).getTitle());

                lastArtistPlayed2.setText(currentTrackInfos.get(1).getArtist());
                lastTitlePlayed2.setText(currentTrackInfos.get(1).getTitle());

                lastArtistPlayed3.setText(currentTrackInfos.get(2).getArtist());
                lastTitlePlayed3.setText(currentTrackInfos.get(2).getTitle());
            }
        }
    }

    private class CurrentTrackListener implements RequestListener<CurrentTrackInfo> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showError(spiceException.getMessage());
        }

        @Override
        public void onRequestSuccess(CurrentTrackInfo currentTrackInfo) {
            if (currentTrackInfo != null){
                ImageLoader.getInstance().displayImage(currentTrackInfo.getCover().getCover600x600(), coverImageView);
                nowPlayingArtistTextView.setText(currentTrackInfo.getArtist());
                nowPlayingTitleTextView.setText(currentTrackInfo.getTitle());
            } else {
                coverImageView.setImageResource(R.drawable.prysm_logo);
                nowPlayingArtistTextView.setText("");
                nowPlayingTitleTextView.setText("");
            }
            TrackHistoryRequest request = new TrackHistoryRequest(1);
            getSpiceManager().execute(request, TrackHistoryRequest.TRACK_HISTORY_REQUEST, DurationInMillis.ONE_MINUTE, new TrackHistoryListener());
        }
    }
}
