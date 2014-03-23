package com.prysmradio.adapters;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.prysmradio.R;
import com.prysmradio.objects.Podcast;

import java.util.List;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class PodcastsAdapter extends BaseAdapter {

    protected SparseArray<View> itemviews;
    private List<Podcast> podcasts;
    private Activity context;

    public PodcastsAdapter(Activity ctx, List<Podcast> list){
        podcasts = list;
        context = ctx;
        itemviews = new SparseArray<View>(list.size());
    }

    @Override
    public int getCount() {
        return podcasts.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (itemviews != null && itemviews.get(position) != null){
            return itemviews.get(position);
        }

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_podcast, null);
        // configure view holder
        TextView title = (TextView) rowView.findViewById(R.id.podcast_title_textView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.podcast_imageView);
        TextView subTitle = (TextView) rowView.findViewById(R.id.podcast_subtitle_textView);

        Podcast p = podcasts.get(position);

        title.setText(p.getTitle());
        subTitle.setText(p.getInfos().getSubtitle());
        ImageLoader.getInstance().displayImage(p.getInfos().getCover(), imageView, new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(300)).build());

        itemviews.put(position, rowView);

        return itemviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return podcasts.get(position);
    }

}
