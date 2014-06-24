package com.prysmradio.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.api.requests.CurrentTrackInfoRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.SendCurrentTrackInfoRequestEvent;
import com.prysmradio.bus.events.UpdateCoverEvent;
import com.prysmradio.bus.events.UpdateCurrentTrackInfoEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.bus.events.UpdatePodcastTitleEvent;
import com.prysmradio.objects.CurrentTrackInfo;
import com.prysmradio.objects.PodcastEpisode;
import com.prysmradio.utils.CurrentStreamInfo;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class BottomPlayerFragment extends PrysmFragment implements RequestListener<CurrentTrackInfo> {

    @InjectView(R.id.play_pause_button)
    ImageView playPauseImageView;
    @InjectView(R.id.bottom_player_artist_textView)
    TextView artistTextView;
    @InjectView(R.id.bottom_player_title_textView)
    TextView titleTextView;

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
        if (((PrysmApplication) getActivity().getApplicationContext()).isServiceIsRunning()){
            if (CurrentStreamInfo.getInstance().getCurrentRadio() != null){
                CurrentTrackInfoRequest currentTrackInfoRequest = new CurrentTrackInfoRequest(CurrentStreamInfo.getInstance().getCurrentRadio().getId());
                getSpiceManager().execute(currentTrackInfoRequest, this);
            } else if (CurrentStreamInfo.getInstance().getPodcastEpisode() != null){
                PodcastEpisode episode = CurrentStreamInfo.getInstance().getPodcastEpisode();
                titleTextView.setText(episode.getSubtitle());
                artistTextView.setText(episode.getTitle());
            }

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        BusManager.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void onSendCurrentTrackInfoRequestEventReceived(SendCurrentTrackInfoRequestEvent event){
        if (event.shouldSendRequest()){
            CurrentTrackInfoRequest request = new CurrentTrackInfoRequest(CurrentStreamInfo.getInstance().getCurrentRadio().getId());
            SpiceManager manager = getSpiceManager();
            if (manager != null){
                manager.execute(request, this);
            }
        }
    }

    public void setPlayPauseImageView(boolean isPlaying){

        playPauseImageView.setImageDrawable(
                        isPlaying ?
                        getResources().getDrawable(R.drawable.ic_action_stop) :
                        getResources().getDrawable(R.drawable.ic_action_play));

        if (!isPlaying){
            titleTextView.setText("");
            artistTextView.setText("");
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
            titleTextView.setText("");
            artistTextView.setText("");
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
            artistTextView.setText(event.getPodcastTitle());
            titleTextView.setText(event.getEpisodeTitle());
        }
    }

    public void setStreamTitleTextView(String streamTitle) {
        this.titleTextView.setText(streamTitle);
    }

    public void setStreamArtistTextView(String streamArtist){
        this.artistTextView.setText(streamArtist);
    }

    public String getStreamTitle(){
        return titleTextView.getText().toString();
    }
    public String getStreamArtist() { return artistTextView.getText().toString(); }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        showError(spiceException.getMessage());
    }

    @Override
    public void onRequestSuccess(CurrentTrackInfo currentTrackInfo) {
        if (currentTrackInfo != null) {
            artistTextView.setText(currentTrackInfo.getArtist());
            titleTextView.setText(currentTrackInfo.getTitle());
            CurrentStreamInfo.getInstance().getCurrentRadio().setArtist(currentTrackInfo.getArtist());
            CurrentStreamInfo.getInstance().getCurrentRadio().setTitle(currentTrackInfo.getTitle());
            CurrentStreamInfo.getInstance().getCurrentRadio().setSongCover(currentTrackInfo.getCover().getCover200x200());
            BusManager.getInstance().getBus().post(new UpdateCurrentTrackInfoEvent(currentTrackInfo));
            ((PrysmApplication) getActivity().getApplication()).getNotificationHandler().updateNotification(currentTrackInfo.getCover().getCover100x100(), currentTrackInfo.getArtist(), currentTrackInfo.getTitle());
        }
    }
}
