package com.prysmradio.adapters;

import android.app.Activity;
import android.view.Gravity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.prysmradio.R;
import com.prysmradio.objects.News;
import com.prysmradio.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            try {
                SimpleDateFormat format = new SimpleDateFormat(Constants.API_DATE_FORMAT);
                Date date = format.parse(n.getDate());

                format.applyPattern(Constants.APP_DATE_FORMAT);
                String dateStr = format.format(date);
                viewHolder.optionalInfo.setText(dateStr);
                viewHolder.optionalInfo.setGravity(Gravity.RIGHT);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ImageLoader.getInstance().displayImage(n.getThumbnail(), viewHolder.thumb);
        }
    }


}
