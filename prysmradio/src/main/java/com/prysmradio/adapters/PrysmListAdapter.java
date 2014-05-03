package com.prysmradio.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prysmradio.R;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public abstract class PrysmListAdapter<E> extends ArrayAdapter<E> {

    protected Activity context;

    public PrysmListAdapter(Activity ctx){
        super(ctx, 0);
        context = ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){

            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title_textView);
            viewHolder.thumb = (ImageView) convertView.findViewById(R.id.thumb_imageView);
            viewHolder.subtitle = (TextView) convertView.findViewById(R.id.subtitle_textView);
            viewHolder.optionalInfo = (TextView) convertView.findViewById(R.id.optional_info_textView);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setupView(viewHolder, position);

        return convertView;
    }



    abstract protected void setupView(ViewHolder viewHolder, int position);

    protected static class ViewHolder {
        ImageView thumb;
        TextView title;
        TextView subtitle;
        TextView optionalInfo;
    }
}
