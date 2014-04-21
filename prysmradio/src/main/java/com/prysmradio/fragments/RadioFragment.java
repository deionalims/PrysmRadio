package com.prysmradio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.prysmradio.R;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.UpdateCoverEvent;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 09/03/14.
 */
public class RadioFragment extends PrysmFragment {

    @InjectView(R.id.cover_imageView)
    ImageView coverImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_radio, container, false);

        ButterKnife.inject(this, v);

        BusManager.getInstance().getBus().register(this);

        return v;
    }

    @Subscribe
    public void onUpdateCoverEventReceived(UpdateCoverEvent event){
        if (event.getCurrentTrackInfo() != null){
            ImageLoader.getInstance().displayImage(event.getCurrentTrackInfo().getCover().getCover600x600(), coverImageView, new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(300)).build());
        } else {
            coverImageView.setImageResource(R.drawable.prysm_logo);
        }

    }
}
