package com.prysmradio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.activities.PlayerActivity;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.EpisodeEvent;
import com.prysmradio.objects.Podcast;
import com.prysmradio.objects.PodcastEpisode;
import com.prysmradio.utils.Constants;

/**
 * Created by fx.oxeda on 24/03/2014.
 */
public class PodcastFragment extends PrysmListFragment<PodcastEpisode> implements RequestListener<Podcast> {

    private Podcast podcast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    void onItemClicked(int position) {
        PodcastEpisode episode = podcast.getEpisodes().get(position);

        Intent intent = new Intent(Constants.START_PODCAST_SERVICE_ACTION);
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

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        showError(spiceException.getMessage());
    }

    @Override
    public void onRequestSuccess(Podcast podcast) {
        progressBar.setVisibility(View.GONE);
        if (podcast != null && podcast.getEpisodes() != null && podcast.getEpisodes().size() > 0){
            notifyDataSetChanged(podcast.getEpisodes());
        }
    }

    public void setPodcast(Podcast podcast) {
        this.podcast = podcast;
    }
}
