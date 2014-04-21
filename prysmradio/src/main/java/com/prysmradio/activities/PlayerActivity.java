package com.prysmradio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.api.ApiManager;
import com.prysmradio.api.requests.CurrentTrackInfoRequest;
import com.prysmradio.api.requests.TrackHistoryRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.TrackHistoryEvent;
import com.prysmradio.bus.events.UpdateCoverEvent;
import com.prysmradio.bus.events.UpdateEpisodeProgressEvent;
import com.prysmradio.bus.events.UpdatePlayerEvent;
import com.prysmradio.objects.CurrentTrackInfo;
import com.prysmradio.objects.PodcastEpisode;
import com.prysmradio.utils.Constants;
import com.squareup.otto.Subscribe;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by fx.oxeda on 07/04/2014.
 */
public class PlayerActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    private PodcastEpisode episode;

    @InjectView(R.id.player_play_pause_button)
    ImageView playPauseImageView;
    @InjectView(R.id.current_position_time)
    TextView currentTimeTextView;
    @InjectView(R.id.total_time) TextView totalTimeTextView;
    @InjectView(R.id.seekBar)
    SeekBar seekBar;
    @InjectView(R.id.player_title_textView) TextView playerTitleTextView;
    @InjectView(R.id.summary_textView) TextView summaryTextView;
    @InjectView(R.id.player_imageView) ImageView playerImageView;
    @InjectView(R.id.radio_infos_layout)
    ViewGroup radioLayout;
    @InjectView(R.id.podcast_infos_layout) ViewGroup podcastLayout;
    @InjectView(R.id.cover_progressBar)
    ProgressBar coverProgressBar;
    @InjectView(R.id.track_history_progressBar) ProgressBar trackHistoryProgressBar;
    @InjectView(R.id.now_playing_imageView) ImageView nowPlayingImageView;
    @InjectView(R.id.now_playing_textView) TextView nowPlayingTextView;
    @InjectView(R.id.track_history_listView)
    ListView trackHistoryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ButterKnife.inject(this);
        setProgressBarIndeterminateVisibility(getIntent().getBooleanExtra(Constants.LOADING_EXTRA, false));
        episode = (PodcastEpisode)getIntent().getSerializableExtra(Constants.EPISODE_EXTRA);

        if (episode != null){
            podcastLayout.setVisibility(View.VISIBLE);
            radioLayout.setVisibility(View.GONE);
            playerTitleTextView.setText(episode.getTitle());
            summaryTextView.setText(episode.getSummary());
            ImageLoader.getInstance().displayImage(episode.getCover(), playerImageView, new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(300)).build());
        } else {
            podcastLayout.setVisibility(View.GONE);
            radioLayout.setVisibility(View.VISIBLE);

            ApiManager.getInstance().invoke(null, new CurrentTrackInfoRequest());
        }

        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusManager.getInstance().getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusManager.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void onUpdatePlayerEventReceived(UpdatePlayerEvent event){
        if (event.isBuffering()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
            setProgressBarIndeterminateVisibility(true);
            playPauseImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
        } else if (event.isPlaying()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
            setProgressBarIndeterminateVisibility(false);
            totalTimeTextView.setText(
                    String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(event.getDuration()),
                            TimeUnit.MILLISECONDS.toSeconds(event.getDuration()) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(event.getDuration()))));
            seekBar.setMax(event.getDuration());
        } else {
            setProgressBarIndeterminateVisibility(false);
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);
            playPauseImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
            seekBar.setProgress(0);
            currentTimeTextView.setText("00:00");
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

    @Subscribe
    public void onUpdateCoverEventReceived(UpdateCoverEvent event){
        coverProgressBar.setVisibility(View.GONE);
        if (event.getCurrentTrackInfo() != null){
            ImageLoader.getInstance().displayImage(event.getCurrentTrackInfo().getCover().getCover600x600(), nowPlayingImageView, new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(300)).build());
            nowPlayingTextView.setText(event.getCurrentTrackInfo().getTitle() + " - " + event.getCurrentTrackInfo().getArtist());
        } else {
            nowPlayingImageView.setImageResource(R.drawable.prysm_logo);
        }
        ApiManager.getInstance().invoke(null, new TrackHistoryRequest());
    }

    @Subscribe
    public void onTrackHistoryEventReceived(TrackHistoryEvent event){
        trackHistoryProgressBar.setVisibility(View.GONE);
        if (event.getTrackHistory() != null && event.getTrackHistory().size() > 0){
            ArrayAdapter<CurrentTrackInfo> adapter = new ArrayAdapter<CurrentTrackInfo>(this,
                    android.R.layout.simple_list_item_2, android.R.id.text1, event.getTrackHistory()){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View row = super.getView(position, convertView, parent);

                    TextView artist = (TextView)row.findViewById(android.R.id.text1);
                    TextView title = (TextView) row.findViewById(android.R.id.text2);

                    artist.setText(getItem(position).getArtist());
                    title.setText(getItem(position).getTitle());

                    return row;
                }
            };
            trackHistoryListView.setAdapter(adapter);
        }
    }

    @OnClick(R.id.player_play_pause_button)
    public void playPauseOnClick(View v){
        if (!((PrysmApplication) getApplicationContext()).isServiceIsRunning()){
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(true);
            Intent intent = new Intent(Constants.START_PODCAST_SERVICE_ACTION);
            intent.putExtra(Constants.AUDIO_URL_EXTRA, episode.getAudioUrl());
            startService(new Intent(intent));
        } else {
            ((PrysmApplication) getApplicationContext()).setServiceIsRunning(false);
            startService(new Intent(Constants.STOP_PODCAST_SERVICE_ACTION));
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            Intent intent = new Intent(Constants.SEEK_PODCAST_SERVICE_ACTION);
            intent.putExtra(Constants.SEEK_TO_EXTRA, progress);
            startService(new Intent(intent));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
