package com.prysmradio.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.adapters.NewsAdapter;
import com.prysmradio.api.requests.NewsRequest;
import com.prysmradio.objects.News;

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

        NewsRequest request = new NewsRequest(0);
        getSpiceManager().execute(request, NewsRequest.NEWS_REQUEST, DurationInMillis.ONE_MINUTE, this);
    }

    @Override
    void onItemClicked(int position) {
        News news = items.get(position);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
        startActivity(browserIntent);
        //Intent intent = new Intent(getActivity(), NewsActivity.class);
//        Intent intent = new Intent(getActivity(), WordPressActivity.class);
//        intent.putExtra(Constants.NEWS_ID_EXTRA, news.getId());
//        getActivity().startActivity(intent);
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
