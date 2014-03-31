package com.prysmradio.adapters;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.prysmradio.R;
import com.prysmradio.objects.PodcastEpisode;

import java.util.List;

/**
 * Created by fx.oxeda on 24/03/2014.
 */
public class EpisodeAdapter extends BaseAdapter {

    protected SparseArray<View> itemviews;
    private List<PodcastEpisode> episodes;
    private Activity context;

    public EpisodeAdapter(Activity ctx, List<PodcastEpisode> list){
        context = ctx;
        episodes = list;
        itemviews = new SparseArray<View>();
    }

    @Override
    public int getCount() {
        return episodes.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (itemviews != null && itemviews.get(position) != null){
            return itemviews.get(position);
        }

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_episode, null);
        // configure view holder
        TextView title = (TextView) rowView.findViewById(R.id.episode_title_textView);
        TextView subTitle = (TextView) rowView.findViewById(R.id.episode_subtitle_textView);

        PodcastEpisode episode = episodes.get(position);

        title.setText(episode.getTitle());
        subTitle.setText(episode.getSubtitle());

        itemviews.put(position, rowView);

        return itemviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return episodes.get(position);
    }


}
