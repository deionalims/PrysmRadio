package com.prysmradio.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.prysmradio.PrysmApplication;
import com.prysmradio.R;
import com.prysmradio.adapters.PodcastsAdapter;
import com.prysmradio.api.ApiManager;
import com.prysmradio.api.PrysmCallback;
import com.prysmradio.api.PrysmRequest;
import com.prysmradio.api.requests.PodcastsRequest;
import com.prysmradio.events.PodcastsListEvent;
import com.prysmradio.objects.Podcast;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

/**
 * Created by fxoxe_000 on 24/03/2014.
 */
public class PodcastsListFragment extends PrysmFragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.podcasts_listView)
    ListView podcastsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_podcasts_list, container, false);

        ButterKnife.inject(this, v);
        ((PrysmApplication)getActivity().getApplicationContext()).getBus().register(this);

        podcastsListView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PodcastsRequest request = new PodcastsRequest(new PrysmCallback<List<Podcast>>() {
            @Override
            public void success(PrysmRequest<List<Podcast>> request, List<Podcast> result) {
                PodcastsAdapter adapter = new PodcastsAdapter(getActivity(), result);
                podcastsListView.setAdapter(adapter);
            }

            @Override
            public void failure(PrysmRequest<List<Podcast>> request, RetrofitError error) {
                Log.v("MICHEL", "failed");
            }
        });
        ApiManager.getInstance().invoke(getActivity(), request);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((PrysmApplication)getActivity().getApplicationContext()).getBus().unregister(this);
    }


    @Subscribe
    public void onPodcastsListEventReceived(PodcastsListEvent event){
        if (event.getPodcasts() != null){
            PodcastsAdapter adapter = new PodcastsAdapter(getActivity(), event.getPodcasts());
            podcastsListView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, new PodcastsListFragment())
                .commit();
    }


}
