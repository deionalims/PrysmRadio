package com.prysmradio.adapters;

import android.app.Activity;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.prysmradio.objects.PodcastEpisode;

/**
 * Created by fx.oxeda on 24/03/2014.
 */
public class EpisodeAdapter extends PrysmListAdapter<PodcastEpisode> {

    public EpisodeAdapter(Activity ctx) {
        super(ctx);
    }

    @Override
    protected void setupView(ViewHolder viewHolder, int position) {
        PodcastEpisode episode = getItem(position);

        if (episode != null){
            viewHolder.title.setText(episode.getTitle());
            viewHolder.subtitle.setText(episode.getSubtitle());
            viewHolder.optionalInfo.setVisibility(View.GONE);

            ImageLoader.getInstance().displayImage(episode.getCover().getCover200x200(), viewHolder.thumb);
        }
    }

}
