package com.prysmradio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.R;
import com.prysmradio.activities.PlayerActivity;
import com.prysmradio.adapters.EpisodeAdapter;
import com.prysmradio.api.requests.EpisodeRequest;
import com.prysmradio.objects.Podcast;
import com.prysmradio.objects.PodcastEpisode;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.CurrentStreamInfo;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new EpisodeAdapter(getActivity());
        listView.setAdapter(adapter);

        String lang = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.pref_lanquage), null);


        if (podcast != null){
            EpisodeRequest request = new EpisodeRequest(lang, podcast.getId());
            getSpiceManager().execute(request, this);
        }

    }

    @Override
    void onItemClicked(int position) {
        PodcastEpisode episode = podcast.getEpisodes().get(position);
        episode.setSummary(podcast.getInfos().getSummary());
        CurrentStreamInfo.getInstance().setPodcastEpisode(episode);

        Intent intent = new Intent(Constants.START_PODCAST_SERVICE_ACTION);
        intent.putExtra(Constants.PODCAST_TITLE_EXTRA, episode.getTitle());
        intent.putExtra(Constants.EPISODE_TITLE_EXTRA, episode.getSubtitle());
        intent.putExtra(Constants.AUDIO_URL_EXTRA, episode.getAudioUrl());

        getActivity().startService(intent);

        Intent podcastIntent = new Intent(getActivity(), PlayerActivity.class);
        podcastIntent.putExtra(Constants.EPISODE_EXTRA, episode);
        podcastIntent.putExtra(Constants.LOADING_EXTRA, true);
        getActivity().startActivityForResult(podcastIntent, Constants.METADATA_REQUEST_CODE);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        showError(spiceException.getMessage());
    }

    @Override
    public void onRequestSuccess(Podcast podcast) {
        progressBar.setVisibility(View.GONE);
        if (podcast != null && podcast.getEpisodes() != null && podcast.getEpisodes().size() > 0){
            this.podcast = podcast;
            notifyDataSetChanged(podcast.getEpisodes());
        }
    }

    public void setPodcast(Podcast podcast) {
        this.podcast = podcast;
    }
}
