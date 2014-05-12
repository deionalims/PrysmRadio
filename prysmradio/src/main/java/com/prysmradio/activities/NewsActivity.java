package com.prysmradio.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.prysmradio.R;
import com.prysmradio.fragments.NewsFragment;
import com.prysmradio.utils.Constants;

/**
 * Created by fxoxe_000 on 12/05/2014.
 */
public class NewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        int newsId = getIntent().getIntExtra(Constants.NEWS_EXTRA, -1);
        NewsFragment fragment = new NewsFragment();
        fragment.setNewsId(newsId);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.news_container, fragment)
                .commit();
    }
}
