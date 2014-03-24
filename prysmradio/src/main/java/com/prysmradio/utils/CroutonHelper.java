package com.prysmradio.utils;

import android.app.Activity;
import android.view.ViewGroup;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by fx.oxeda on 25/03/2014.
 */
public class CroutonHelper {

    private static final Configuration LONG_CONFIG;

    static {
        LONG_CONFIG = new Configuration.Builder()
                .setDuration(Configuration.DURATION_LONG)
                .build();
    }

    public static void alert(Activity activity, String text) {
        clearAndMakeText(activity, text, Style.ALERT, null, false);
    }

    public static void info(Activity activity, String text) {
        clearAndMakeText(activity, text, Style.INFO, null, false);
    }

    public static void clearAndMakeText(Activity activity, String text, Style style, ViewGroup view, boolean isLong) {
        if (null == text){
            return;
        }
        Crouton.clearCroutonsForActivity(activity);
        Crouton.makeText(activity, text, style, view)
                .setConfiguration(isLong ? LONG_CONFIG : Configuration.DEFAULT)
                .show();
    }
}
