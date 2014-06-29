package com.prysmradio.activities;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.facebook.widget.FacebookDialog;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.R;
import com.prysmradio.api.requests.NewsDetailsRequest;
import com.prysmradio.objects.News;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.Utils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 12/05/2014.
 */
public class NewsActivity extends BaseActivity implements RequestListener<News> {

    @InjectView(R.id.news_progressBar)
    ProgressBar newsProgressBar;
    @InjectView(R.id.news_webView)
    WebView newsWebView;

    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        int newsId = getIntent().getIntExtra(Constants.NEWS_EXTRA, -1);

        ButterKnife.inject(this);

        newsWebView.getSettings().setJavaScriptEnabled(true);
        newsWebView.setWebViewClient(new WebViewClient());

        String lang = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_lanquage), null);

        NewsDetailsRequest request = new NewsDetailsRequest(newsId, lang);
        getSpiceManager().execute(request, this);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        newsProgressBar.setVisibility(View.GONE);
        showError(spiceException.getMessage());
    }

    @Override
    public void onRequestSuccess(News news) {
        newsProgressBar.setVisibility(View.GONE);
        this.news = news;
        newsWebView.loadUrl(news.getGuid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PrysmSettingsActivity.class);
            startActivity(intent);
            return true;
        }

        switch (id){
            case R.id.facebook:
                shareFacebookNews();
                break;
            case R.id.twitter:
                shareTwitterNews();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void shareFacebookNews(){
        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                .setLink(news.getGuid())
                .setApplicationName("Prysm Radio France")
                .build();
        uiHelper.trackPendingDialogCall(shareDialog.present());
    }

    protected void shareTwitterNews(){

        String tweetUrl =
                String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                        Utils.urlEncode(news.getTitle()), Utils.urlEncode(news.getGuid()));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(intent);
    }
}
