package com.prysmradio.adapters;

import android.app.Activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.prysmradio.R;
import com.prysmradio.objects.Podcast;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class PodcastsAdapter extends PrysmListAdapter<Podcast> {

    public PodcastsAdapter(Activity ctx) {
        super(ctx);
    }

    @Override
    protected void setupView(ViewHolder viewHolder, int position) {
        Podcast podcast = getItem(position);

        if (podcast != null){
            viewHolder.title.setText(podcast.getTitle());
            viewHolder.subtitle.setText(podcast.getInfos().getSubtitle());
            viewHolder.optionalInfo.setText(String.format(context.getString(R.string.number_episodes), podcast.getEpisodeNum()));

            ImageLoader.getInstance().displayImage(podcast.getInfos().getCover().getCover200x200(), viewHolder.thumb);
        }

    }
}
