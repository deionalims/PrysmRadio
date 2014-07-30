package com.prysmradio.activities;

import android.os.Bundle;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.prysmradio.R;
import com.prysmradio.objects.News;
import com.prysmradio.objects.NewsAttachment;
import com.prysmradio.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 28/07/2014.
 */
public class ImageGalleryActivity extends BaseActivity {

    @InjectView(R.id.slider) SliderLayout sliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        ButterKnife.inject(this);

        News news = (News) getIntent().getSerializableExtra(Constants.NEWS_EXTRA);

        if (news.getAttachment() != null && news.getAttachment().size() > 0){
            for (NewsAttachment na : news.getAttachment()){
                DefaultSliderView sliderView = new DefaultSliderView(this);
                sliderView.setScaleType(BaseSliderView.ScaleType.CenterInside);
                sliderView.image(na.getMedium());
                sliderLayout.addSlider(sliderView);
            }
        }
    }

}
