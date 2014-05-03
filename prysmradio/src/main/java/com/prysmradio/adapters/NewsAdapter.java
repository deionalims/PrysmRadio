package com.prysmradio.adapters;

import android.app.Activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.prysmradio.R;
import com.prysmradio.objects.News;

/**
 * Created by fxoxe_000 on 29/04/2014.
 */
public class NewsAdapter extends PrysmListAdapter<News> {

    public NewsAdapter(Activity ctx){
        super(ctx);
    }

    @Override
    protected void setupView(ViewHolder viewHolder, int position) {
        News n = getItem(position);

        if (n != null){
            viewHolder.title.setText(n.getTitle());
            viewHolder.subtitle.setText(String.format(context.getString(R.string.by_author), n.getAuthor()));
            viewHolder.optionalInfo.setText(n.getDate());


            ImageLoader.getInstance().displayImage(n.getThumb(), viewHolder.thumb);
        }
    }


}
