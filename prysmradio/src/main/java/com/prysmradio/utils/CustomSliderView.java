package com.prysmradio.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.prysmradio.R;

/**
 * Created by fxoxe_000 on 28/07/2014.
 */
public class CustomSliderView extends BaseSliderView {

    public CustomSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_default,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        target.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        bindEventAndShow(v, target);
        return v;
    }
}
