package com.prysmradio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.prysmradio.R;
import com.prysmradio.adapters.NewsAdapter;
import com.prysmradio.api.ApiManager;
import com.prysmradio.api.requests.NewsRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.NewsEvent;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fx.oxeda on 31/03/2014.
 */
public class PrysmContentFragment extends PrysmFragment {

    @InjectView(R.id.news_listView)
    ListView listView;
    @InjectView(R.id.news_progressBar)
    ProgressBar progressBar;

    private NewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news, container, false);

        ButterKnife.inject(this, v);

        adapter = new NewsAdapter(getActivity());
        listView.setAdapter(adapter);

        BusManager.getInstance().getBus().register(this);

        ApiManager.getInstance().invoke(getActivity(), new NewsRequest(0));

        return v;
    }

    @Subscribe
    public void onNewsEventReceived(NewsEvent event){
        if (event != null && event.getNewsList().size() > 0){
            progressBar.setVisibility(View.GONE);
            adapter.addAll(event.getNewsList());
            adapter.notifyDataSetChanged();
        }
    }

}
