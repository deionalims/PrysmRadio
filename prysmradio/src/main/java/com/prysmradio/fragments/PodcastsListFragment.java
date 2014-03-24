package com.prysmradio.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.prysmradio.R;
import com.prysmradio.adapters.PodcastsAdapter;
import com.prysmradio.api.ApiManager;
import com.prysmradio.api.requests.PodcastsRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.PodcastsListEvent;
import com.prysmradio.objects.Podcast;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 24/03/2014.
 */
public class PodcastsListFragment extends PrysmFragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.podcasts_listView)
    ListView podcastsListView;
    @InjectView(R.id.podcasts_list_progressBar)
    ProgressBar progressBar;

    private List<Podcast> podcasts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_podcasts_list, container, false);

        ButterKnife.inject(this, v);
        BusManager.getInstance().getBus().register(this);

        podcastsListView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PodcastsRequest request = new PodcastsRequest();
        ApiManager.getInstance().invoke(getActivity(), request);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusManager.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void onPodcastsListEventReceived(PodcastsListEvent event) {
        progressBar.setVisibility(View.GONE);
        if (event.getPodcasts() != null){
            podcasts = event.getPodcasts();
            PodcastsAdapter adapter = new PodcastsAdapter(getActivity(), event.getPodcasts());
            podcastsListView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Podcast podcast = podcasts.get(position);

        PodcastFragment fragment = new PodcastFragment();
        fragment.setPodcast(podcast);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("toto")
                .commit();
    }


}
