package com.prysmradio.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.R;
import com.prysmradio.adapters.PodcastsAdapter;
import com.prysmradio.api.requests.PodcastsRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.PodcastsListEvent;
import com.prysmradio.objects.Podcast;
import com.squareup.otto.Subscribe;

/**
 * Created by fxoxe_000 on 24/03/2014.
 */
public class PodcastsListFragment extends PrysmListFragment<Podcast> implements RequestListener<Podcast.List> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new PodcastsAdapter(getActivity());
        listView.setAdapter(adapter);

        PodcastsRequest request = new PodcastsRequest();
        getSpiceManager().execute(request, PodcastsRequest.PODCAST_REQUEST, DurationInMillis.ONE_MINUTE, this);
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
    public void onPodcastsListEventReceived(PodcastsListEvent event) {
        progressBar.setVisibility(View.GONE);
        if (event.getPodcasts() != null){
            items = event.getPodcasts();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestSuccess(Podcast.List podcasts) {
        progressBar.setVisibility(View.GONE);
        if (podcasts != null && podcasts.size() > 0){
            notifyDataSetChanged(podcasts);
        }
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        showError(spiceException.getMessage());
    }

    @Override
    void onItemClicked(int position) {
        Podcast podcast = items.get(position);

        PodcastFragment fragment = new PodcastFragment();
        fragment.setPodcast(podcast);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("toto")
                .commit();
    }
}
