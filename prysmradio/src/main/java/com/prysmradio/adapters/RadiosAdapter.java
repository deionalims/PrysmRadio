package com.prysmradio.adapters;

import android.app.Activity;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.prysmradio.objects.Radio;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class RadiosAdapter extends PrysmListAdapter<Radio> {

    public RadiosAdapter(Activity ctx) {
        super(ctx);
    }

    @Override
    protected void setupView(ViewHolder viewHolder, int position) {
        Radio radio = getItem(position);

        if (radio != null){
            viewHolder.title.setText(radio.getName());
            viewHolder.subtitle.setText(radio.getDescription());
            viewHolder.optionalInfo.setVisibility(View.GONE);

            ImageLoader.getInstance().displayImage(radio.getCover().getCover200x200(), viewHolder.thumb);
        }
    }
}
