package com.prysmradio.activities;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.facebook.widget.FacebookDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.R;
import com.prysmradio.api.requests.NewsDetailsRequest;
import com.prysmradio.objects.News;
import com.prysmradio.objects.NewsAttachment;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 12/05/2014.
 */
public class NewsActivity extends BaseActivity implements RequestListener<News>, BaseSliderView.OnSliderClickListener {

    private static String POSITION = "position";

    @InjectView(R.id.news_progressBar) ProgressBar newsProgressBar;
    @InjectView(R.id.news_image) ImageView newsImage;
    @InjectView(R.id.news_title) TextView newsTitle;
    @InjectView(R.id.news_author) TextView newsAuthor;
    @InjectView(R.id.news_date) TextView newsDate;
    @InjectView(R.id.news_content) TextView newsContent;
    @InjectView(R.id.slider) SliderLayout sliderLayout;

    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        int newsId = getIntent().getIntExtra(Constants.NEWS_EXTRA, -1);

        ButterKnife.inject(this);

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);


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
        setupPage();
    }

    private void setupPage(){
        ImageLoader.getInstance().displayImage(news.getImageHeader(), newsImage);

        newsTitle.setText(news.getTitle());
        newsAuthor.setText(String.format(getString(R.string.by_author), news.getAuthor()));
        try {
            SimpleDateFormat format = new SimpleDateFormat(Constants.API_DATE_FORMAT);
            Date date = format.parse(news.getDate());

            format.applyPattern(Constants.APP_DATE_FORMAT);
            String dateStr = format.format(date);
            newsDate.setText(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            showError("Date ParseException: " + e.getMessage());
        }

        newsContent.setText(Html.fromHtml(news.getPseudoHTML()));

        if (news.getAttachment() != null && news.getAttachment().size() > 0){
            int i = 0;
            for (NewsAttachment na : news.getAttachment()){
                DefaultSliderView sliderView = new DefaultSliderView(this);
                sliderView
                        .image(na.getMedium())
                        .setOnSliderClickListener(this);
                sliderView.getBundle().putInt(POSITION, i);
                sliderLayout.addSlider(sliderView);
                i++;
            }
            sliderLayout.setVisibility(View.VISIBLE);
        }
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
                .setLink(news.getUrl())
                .setApplicationName("Prysm Radio France")
                .build();
        uiHelper.trackPendingDialogCall(shareDialog.present());
    }

    protected void shareTwitterNews(){

        String tweetUrl =
                String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                        Utils.urlEncode(news.getTitle()), Utils.urlEncode(news.getUrl()));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(intent);
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        Intent intent = new Intent(this, ImageGalleryActivity.class);
        intent.putExtra(Constants.NEWS_EXTRA, news);
        intent.putExtra(Constants.POSITION_EXTRA, baseSliderView.getBundle().getInt(POSITION));
        startActivity(intent);
    }
}
