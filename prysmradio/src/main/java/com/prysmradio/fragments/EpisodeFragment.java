package com.prysmradio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdateEpisodeProgressEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.bus.events.UpdatePodcastTitleEvent;
import com.prysmradio.objects.PodcastEpisode;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.CurrentStreamInfo;
import com.squareup.otto.Subscribe;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 04/05/2014.
 */
public class EpisodeFragment extends PrysmFragment implements SeekBar.OnSeekBarChangeListener {

    @InjectView(R.id.current_position_time) TextView currentTimeTextView;
    @InjectView(R.id.total_time) TextView totalTimeTextView;
    @InjectView(R.id.seekBar) SeekBar seekBar;
    @InjectView(R.id.summary_textView) TextView summaryTextView;
    @InjectView(R.id.now_playing_imageView)
    ImageView coverImageView;
    @InjectView(R.id.now_playing_artist_textView) TextView nowPlayingTitleTextView;
    @InjectView(R.id.now_playing_title_textView) TextView nowPlayingSubtitleTextView;

    PodcastEpisode episode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_podcast_episode, container, false);

        ButterKnife.inject(this, v);

        seekBar.setOnSeekBarChangeListener(this);

        episode = CurrentStreamInfo.getInstance().getPodcastEpisode();

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (episode != null){
            summaryTextView.setText(episode.getSummary());
            nowPlayingTitleTextView.setText(episode.getTitle());
            nowPlayingSubtitleTextView.setText(episode.getSubtitle());
            ImageLoader.getInstance().displayImage(episode.getCover(), coverImageView);
            seekBar.setMax((int)episode.getDuration());
            totalTimeTextView.setText(
                    String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes((int)episode.getDuration()),
                            TimeUnit.MILLISECONDS.toSeconds((int)episode.getDuration()) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((int)episode.getDuration()))));

            if (((PrysmApplication) getActivity().getApplicationContext()).isServiceIsRunning()){
                getActivity().setProgressBarIndeterminateVisibility(false);
            }

        }
    }

    @Subscribe
    public void onUpdatePlayerEventReceived(UpdatePlayerEvent event){
        if (event.isPlaying()){
            getActivity().setProgressBarIndeterminateVisibility(false);
            totalTimeTextView.setText(
                    String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(event.getDuration()),
                            TimeUnit.MILLISECONDS.toSeconds(event.getDuration()) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(event.getDuration()))));
            seekBar.setMax(event.getDuration());
            BusManager.getInstance().getBus().post(new UpdatePodcastTitleEvent(episode.getTitle(), episode.getSubtitle()));
        }
    }

    @Subscribe
    public void onUpdateProgressEventReceived(UpdateEpisodeProgressEvent event){
        currentTimeTextView.setText(
                String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(event.getProgress()),
                        TimeUnit.MILLISECONDS.toSeconds(event.getProgress()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(event.getProgress()))));
        seekBar.setProgress(event.getProgress());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            Intent intent = new Intent(Constants.SEEK_PODCAST_SERVICE_ACTION);
            intent.putExtra(Constants.SEEK_TO_EXTRA, progress);
            getActivity().startService(new Intent(intent));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
