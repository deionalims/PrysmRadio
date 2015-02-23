package com.prysmradio.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.prysmradio.R;

import butterknife.ButterKnife;

/**
 * Created by FX on 23/02/15.
 */
public class DrawerView extends LinearLayout {

    public DrawerView(Context context) {
        super(context);

        init();
    }

    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init(){
        View v = View.inflate(getContext(), R.layout.view_drawer, this);

        ButterKnife.inject(this, v);
    }
}
