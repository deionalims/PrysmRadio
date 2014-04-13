package com.prysmradio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.prysmradio.R;
import com.prysmradio.activities.PlayerActivity;
import com.prysmradio.adapters.EpisodeAdapter;
import com.prysmradio.api.ApiManager;
import com.prysmradio.api.requests.EpisodeRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.EpisodeEvent;
import com.prysmradio.bus.events.PodcastEvent;
import com.prysmradio.bus.events.RetroFitErrorEvent;
import com.prysmradio.objects.Podcast;
import com.prysmradio.objects.PodcastEpisode;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.CroutonHelper;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fx.oxeda on 24/03/2014.
 */
public class PodcastFragment extends PrysmFragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.podcast_imageView)
    ImageView podcastImageView;
    @InjectView(R.id.podcast_title_textView)
    TextView titleTextView;
    @InjectView(R.id.podcast_subtitle_textView)
    TextView subtitleTextView;
    @InjectView(R.id.episode_listView)
    ListView episodesListView;
    @InjectView(R.id.episodes_progressBar)
    ProgressBar progressBar;

    private Podcast podcast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_podcast, container, false);

        ButterKnife.inject(this, v);
        BusManager.getInstance().getBus().register(this);

        episodesListView.setOnItemClickListener(this);

        ImageLoader.getInstance().displayImage(podcast.getInfos().getCover(), podcastImageView, new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(300)).build());
        titleTextView.setText(podcast.getTitle());
        subtitleTextView.setText(podcast.getInfos().getSubtitle());

        BusManager.getInstance().getBus().register(this);
        ApiManager.getInstance().invoke(getActivity(), new EpisodeRequest(podcast));

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusManager.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void onPodcastEvent(PodcastEvent event){
        progressBar.setVisibility(View.GONE);
        if (event.getPodcast() != null){
            podcast = event.getPodcast();
            EpisodeAdapter adapter = new EpisodeAdapter(getActivity(), event.getPodcast().getEpisodes());
            episodesListView.setAdapter(adapter);
        }
    }

    @Subscribe
    public void onRetroFitErrorEvent(RetroFitErrorEvent event){
        progressBar.setVisibility(View.GONE);
        CroutonHelper.alert(getActivity(), event.getError().getMessage());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PodcastEpisode episode = podcast.getEpisodes().get(i);

        Intent intent = new Intent(Constants.START_SERVICE_ACTION);
        intent.putExtra(Constants.PODCAST_TITLE_EXTRA, episode.getTitle());
        intent.putExtra(Constants.EPISODE_TITLE_EXTRA, episode.getSubtitle());
        intent.putExtra(Constants.AUDIO_URL_EXTRA, episode.getAudioUrl());

        getActivity().startService(intent);

        episode.setSummary(podcast.getInfos().getSummary()  );
        Intent podcastIntent = new Intent(getActivity(), PlayerActivity.class);
        podcastIntent.putExtra(Constants.EPISODE_EXTRA, episode);
        podcastIntent.putExtra(Constants.LOADING_EXTRA, true);
        getActivity().startActivity(podcastIntent);

        BusManager.getInstance().getBus().post(new EpisodeEvent(episode));
    }

    public void setPodcast(Podcast podcast) {
        this.podcast = podcast;
    }
}
