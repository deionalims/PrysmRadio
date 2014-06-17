package com.prysmradio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.R;
import com.prysmradio.activities.NewsActivity;
import com.prysmradio.adapters.NewsAdapter;
import com.prysmradio.api.requests.NewsRequest;
import com.prysmradio.objects.News;
import com.prysmradio.utils.Constants;

/**
 * Created by fx.oxeda on 31/03/2014.
 */
public class NewsListFragment extends PrysmListFragment<News> implements RequestListener<News.List> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new NewsAdapter(getActivity());
        listView.setAdapter(adapter);

        String lang = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.pref_lanquage), null);

        NewsRequest request = new NewsRequest(0, lang);
        getSpiceManager().execute(request, NewsRequest.NEWS_REQUEST, DurationInMillis.ONE_MINUTE, this);
    }

    @Override
    void onItemClicked(int position) {
        News news = items.get(position);
        Intent intent = new Intent(getActivity(), NewsActivity.class);
        intent.putExtra(Constants.NEWS_EXTRA, news.getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        showError(spiceException.getMessage());
    }

    @Override
    public void onRequestSuccess(News.List news) {
        progressBar.setProgress(View.GONE);
        if (news != null && news.size() > 0){
            notifyDataSetChanged(news);
        }
    }
}
