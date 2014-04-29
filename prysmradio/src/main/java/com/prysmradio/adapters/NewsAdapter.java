package com.prysmradio.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.prysmradio.R;
import com.prysmradio.objects.News;

/**
 * Created by fxoxe_000 on 29/04/2014.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    private Activity context;

    public NewsAdapter(Activity ctx){
        super(ctx, 0);
        context = ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){

            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_news, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.news_title_textView);
            viewHolder.thumb = (ImageView) convertView.findViewById(R.id.news_thumb);
            viewHolder.author = (TextView) convertView.findViewById(R.id.news_author_textView);
            viewHolder.date = (TextView) convertView.findViewById(R.id.news_date);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        News n = getItem(position);

        if (n != null){
            viewHolder.title.setText(n.getTitle());
            viewHolder.author.setText(String.format(context.getString(R.string.by_author), n.getAuthor()));
            viewHolder.date.setText(n.getDate());


            ImageLoader.getInstance().displayImage(n.getThumb(), viewHolder.thumb);
        }

        return convertView;
    }


    private static class ViewHolder {
        ImageView thumb;
        TextView title;
        TextView author;
        TextView date;
    }
}
