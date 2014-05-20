package com.prysmradio.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.prysmradio.R;

/**
 * Created by fx.oxeda on 20/05/2014.
 */
public class PrysmSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
