package com.prysmradio.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.R;
import com.prysmradio.activities.BaseActivity;
import com.prysmradio.api.requests.NewsDetailsRequest;
import com.prysmradio.objects.News;
import com.prysmradio.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 12/05/2014.
 */
public class NewsFragment extends PrysmFragment implements RequestListener<News> {

    @InjectView(R.id.news_progressBar)
    ProgressBar newsProgressBar;
    @InjectView(R.id.news_webView)
    WebView newsWebView;

    private int newsId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        ButterKnife.inject(this, v);

        newsWebView.getSettings().setJavaScriptEnabled(true);
        newsWebView.setWebViewClient(new WebViewClient());

        String lang = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.LANGUAGE_PREF, null);

        NewsDetailsRequest request = new NewsDetailsRequest(newsId, lang);
        getSpiceManager().execute(request, this);

        return v;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        newsProgressBar.setVisibility(View.GONE);
        showError(spiceException.getMessage());
    }

    @Override
    public void onRequestSuccess(News news) {
        newsProgressBar.setVisibility(View.GONE);

        newsWebView.loadUrl(news.getGuid());
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    protected BaseActivity getBaseActivity(){
        return (BaseActivity) getActivity();
    }

    protected SpiceManager getSpiceManager(){
        BaseActivity activity = getBaseActivity();
        if (activity != null){
            return activity.getSpiceManager();
        }
        return null;
    }
}
