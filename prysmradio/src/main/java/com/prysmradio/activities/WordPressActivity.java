package com.prysmradio.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.R;
import com.prysmradio.api.requests.NewsDetailsRequest;
import com.prysmradio.objects.News;
import com.prysmradio.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 18/08/2014.
 */
public class WordPressActivity extends BaseActivity implements RequestListener<News> {

    @InjectView(R.id.webview) WebView webView;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordpress);

        ButterKnife.inject(this);

        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        int newsId = getIntent().getIntExtra(Constants.NEWS_ID_EXTRA, -1);

        String lang = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_lanquage), null);

        NewsDetailsRequest request = new NewsDetailsRequest(newsId, lang);
        getSpiceManager().execute(request, this);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(News news) {
        this.news = news;
        webView.loadUrl(news.getUrl());
//        webView.loadDataWithBaseURL(null, news.getContent(),
//                "text/html; charset=utf-8", "UTF-8", null);
    }
}
