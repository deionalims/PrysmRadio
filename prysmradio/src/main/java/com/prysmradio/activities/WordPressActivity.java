package com.prysmradio.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.R;
import com.prysmradio.api.requests.NewsDetailsRequest;
import com.prysmradio.objects.News;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.HTML5WebView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 18/08/2014.
 */
public class WordPressActivity extends BaseActivity implements RequestListener<News> {

    @InjectView(R.id.wordpress_webView)
    HTML5WebView webView;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordpress);

        ButterKnife.inject(this);

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
        webView.loadData(news.getPseudoHTML(), "text/html", "UTF-8");
    }
}
